package InfrastructureManager.Modules.Diagnostics.Output;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.Diagnostics.DiagnosticsModuleObject;
import InfrastructureManager.Modules.Diagnostics.Exception.Instruction.InstructionFileLoadingException;
import InfrastructureManager.Modules.Diagnostics.Exception.Instruction.InstructionHandlingException;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.Instruction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class InstructionOutput extends DiagnosticsModuleObject implements PlatformOutput {

    public InstructionOutput(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    @Override
    public void execute(String response) throws ModuleExecutionException {
        String[] command = response.split(" ");
        if (command[0].equals("instruction")) {
            try {
                switch (command[1]) {
                    case "loadInstruction" -> {
                        Instruction instruction = instructionFromFile(command[2],command[3]);
                        addToInstructionList(instruction);
                    }
                    case "loadExperiment" -> executeExperiment(command[2]);
                    default -> throw new InstructionHandlingException("Invalid command " + command[1] + " for InstructionOutput");
                }

            } catch (IndexOutOfBoundsException e) {
                throw new InstructionHandlingException("Arguments missing for command " + response  + " to InstructionOutput");
            }
        }
    }

    private Instruction instructionFromFile(String clientInstructionPath, String serverInstructionPath) throws InstructionFileLoadingException {
        try {
            File clientFile = new File(clientInstructionPath);
            File serverFile = new File(serverInstructionPath);
            if (!clientFile.exists() || !serverFile.exists()) {
                throw new InstructionFileLoadingException("Instruction files were not found!");
            }
            String clientInstruction = Files.readString(clientFile.toPath()).replaceAll("\\s+","");
            String serverInstruction = Files.readString(serverFile.toPath()).replaceAll("\\s+","");
            return new Instruction(clientInstruction,serverInstruction);
        } catch (IOException e) {
            throw new InstructionFileLoadingException("Instruction files could not be read",e);
        }
    }

    private void addToInstructionList(Instruction instruction) throws InstructionHandlingException {
        try {
            this.getInstructionList().addInstruction(instruction);
        } catch (InterruptedException e) {
            throw new InstructionHandlingException("Output interrupted while waiting on instruction list");
        }
    }


    private void executeExperiment(String path) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode experiment = mapper.readTree(new File(path));
            String name = experiment.get("name").asText();
            System.out.println("Executing " + name + " with diagnostics module");
            ObjectNode clientBase = (ObjectNode) experiment.get("client").get("base");
            JsonNode clientChanges = experiment.get("client").get("varying");
            ObjectNode serverBase = (ObjectNode) experiment.get("server").get("base");
            JsonNode serverChanges = experiment.get("server").get("varying");

            List<String> clientInstructions = getInstructionList(clientBase,clientChanges);
            List<String> serverInstructions = getInstructionList(serverBase,serverChanges);

            int clientIndex = 0;
            int serverIndex = 0;
            int clientSize = clientInstructions.size();
            int serverSize = serverInstructions.size();
            if (clientSize != serverSize && clientSize != 1 && serverSize != 1) {
                //TODO: THROW EXCEPTION, INVALID EXPERIMENT
                System.err.println("Experiment is invalid in length!");
                return;
            }
            int limit = Integer.max(clientSize, serverSize);
            ObjectNode initialInstruction = mapper.createObjectNode();
            initialInstruction.put("experimentName", name);
            initialInstruction.put("experimentLength", limit);
            String initialInstructionString = initialInstruction.toString();
            addToInstructionList(new Instruction(initialInstructionString,initialInstructionString));
            
            for (int i = 0; i < limit; i++) {
                String clientInstruction = clientInstructions.get(clientIndex);
                String serverInstruction = serverInstructions.get(serverIndex);
                Instruction instruction = new Instruction(clientInstruction, serverInstruction);
                addToInstructionList(instruction);
                clientIndex = clientSize > 1 ? clientIndex + 1 : 0;
                serverIndex = serverSize > 1 ? serverSize + 1 : 0;
            }

        } catch (IOException | InstructionHandlingException e) {
            e.printStackTrace(); //TODO: HANDLE
        }
    }

    private List<String> getInstructionList(ObjectNode base, JsonNode changes) {
        List<String> result = new ArrayList<>();
        if (!changes.isEmpty()) {
            String object = changes.get("object").asText();
            String field = changes.get("field").asText();
            ObjectNode nodeToBeChanged = (ObjectNode) base.findValue(object);

            int from = changes.get("from").asInt();
            int to = changes.get("to").asInt();
            int step = changes.get("step").asInt();
            for (int i = from; i <= to; i+=step) {
                nodeToBeChanged.put(field, i);
                result.add(base.toString());
            }
        } else {
            result.add(base.toString());
        }
        return result;
    }
}
