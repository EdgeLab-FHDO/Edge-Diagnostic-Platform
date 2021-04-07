package InfrastructureManager.Modules.Diagnostics.Output;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.Diagnostics.DiagnosticsModuleObject;
import InfrastructureManager.Modules.Diagnostics.Exception.Instruction.InstructionFileLoadingException;
import InfrastructureManager.Modules.Diagnostics.Exception.Instruction.InstructionHandlingException;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.AdvantEdgeInstruction;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.Instruction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

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

    private void executeExperiment(String path) throws InstructionFileLoadingException {
        ObjectMapper mapper = new ObjectMapper();
        File experimentFile = new File(path);
        if (!experimentFile.exists()) throw new InstructionFileLoadingException("Experiment file not found!");
        try {
            JsonNode experiment = mapper.readTree(experimentFile);
            String name = experiment.get("name").asText();
            System.out.println("Executing " + name + " with diagnostics module");
            Map<String, List<String>> instructions = getInstructionLists(experiment);

            List<String> clientInstructions = instructions.get("client");
            List<String> serverInstructions = instructions.get("server");

            List<String> aeInstructions = instructions.getOrDefault("ae",null);
            List<String> computeInstructions = instructions.getOrDefault("compute", null);


            int experimentLength = clientInstructions.size();
            ObjectNode initialInstruction = mapper.createObjectNode();
            initialInstruction.put("experimentName", name);
            initialInstruction.put("experimentLength", experimentLength);
            String initialInstructionString = initialInstruction.toString();
            //addToInstructionList(new Instruction(initialInstructionString,initialInstructionString));

            for (int i = 0; i < experimentLength; i++) {
                if (aeInstructions != null) System.out.println(aeInstructions.get(i));
                if (computeInstructions != null) System.out.println(computeInstructions.get(i));
                String clientInstruction = clientInstructions.get(i);
                String serverInstruction = serverInstructions.get(i);
                Instruction instruction = new Instruction(clientInstruction, serverInstruction);
                //addToInstructionList(instruction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, List<String>> getInstructionLists(JsonNode experiment) {
        Map<String, List<String>> result = new HashMap<>();
        ObjectNode clientBase = (ObjectNode) experiment.get("client").get("base");
        JsonNode clientChanges = experiment.get("client").get("varying");
        ObjectNode serverBase = (ObjectNode) experiment.get("server").get("base");
        JsonNode serverChanges = experiment.get("server").get("varying");
        JsonNode computeChanges = experiment.get("compute");
        JsonNode networkChanges = experiment.get("network");

        List<String> aeInstructions = new ArrayList<>();
        List<String> computeInstructions = new ArrayList<>();
        List<String> clientInstructions = new ArrayList<>();
        List<String> serverInstructions = new ArrayList<>();

        boolean changesInClient = !clientChanges.isEmpty();
        boolean changesInServer = !serverChanges.isEmpty();
        boolean changesInCompute = !computeChanges.isEmpty();
        boolean changesInNetwork = !networkChanges.isEmpty();

        if (!changesInClient && !changesInServer) {
            int size = 0;
            if (!changesInCompute && !changesInNetwork) {
                // Invalid experiment nothing changes //TODO: Custom exception
            } else if (changesInNetwork) {
                aeInstructions = getAEInstructionList(networkChanges);
                size = aeInstructions.size();
            } else {
                computeInstructions = getComputeInstructionList(computeChanges);
                size = computeInstructions.size();
            }
            clientInstructions = getRepeatedClientServerInstructionList(clientBase, size);
            serverInstructions = getRepeatedClientServerInstructionList(serverBase, size);
        } else if(changesInClient) {
            clientInstructions = getChangingClientServerInstructionList(clientBase,clientChanges);
            serverInstructions = getRepeatedClientServerInstructionList(serverBase, clientInstructions.size());
        } else {
            serverInstructions = getChangingClientServerInstructionList(serverBase, serverChanges);
            clientInstructions = getRepeatedClientServerInstructionList(clientBase, serverInstructions.size());
        }
        result.put("client", clientInstructions);
        result.put("server", serverInstructions);
        if (!aeInstructions.isEmpty()) result.put("ae", aeInstructions);
        if (!computeInstructions.isEmpty()) result.put("compute", computeInstructions);
        return result;
    }

    private List<String> getRepeatedClientServerInstructionList(ObjectNode base, int size) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(base.toString());
        }
        return result;
    }

    private List<String> getChangingClientServerInstructionList(ObjectNode base, JsonNode changes) {
        List<String> result = new ArrayList<>();
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
        return result;
    }

    private List<String> getAEInstructionList(JsonNode networkChanges){
        String scenarioName = networkChanges.get("scenarioName").asText();
        String elementName = networkChanges.get("elementName").asText();
        String elementType = networkChanges.get("elementType").asText();
        String fieldToChange = networkChanges.get("field").asText();
        int from = networkChanges.get("from").asInt();
        int to = networkChanges.get("to").asInt();
        int step = networkChanges.get("step").asInt();

        AdvantEdgeInstruction instruction = new AdvantEdgeInstruction(scenarioName,
                elementName, elementType);
        List<String> aeInstructions = new ArrayList<>();
        for (int i = from; i <= to; i+= step) {
            instruction.changeValue(fieldToChange,i);
            aeInstructions.add(instruction.toString());
        }
        return aeInstructions;
    }

    private List<String> getComputeInstructionList(JsonNode computeChanges) {
        int from = computeChanges.get("from").asInt();
        int to = computeChanges.get("to").asInt();
        int step = computeChanges.get("step").asInt();
        return IntStream.iterate(from,n -> n >= to, n -> n + step)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }
}
