package InfrastructureManager.Modules.Diagnostics.Output;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.Diagnostics.DiagnosticsModuleObject;
import InfrastructureManager.Modules.Diagnostics.Exception.Instruction.InstructionFileLoadingException;
import InfrastructureManager.Modules.Diagnostics.Exception.Instruction.InstructionHandlingException;
import InfrastructureManager.Modules.Diagnostics.InstructionLock;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.AdvantEdgeInstruction;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.ApplicationInstruction;
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
import java.util.stream.IntStream;

public class InstructionOutput extends DiagnosticsModuleObject implements PlatformOutput {

    private static final int WAITING_TIME = 1000;
    private final InstructionLock instructionLock;
    private boolean clientNextFlag;
    private boolean serverNextFlag;

    public InstructionOutput(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
        instructionLock = new InstructionLock(ownerModule);
        clientNextFlag = false;
        serverNextFlag = false;
    }

    @Override
    public void execute(String response) throws ModuleExecutionException {
        String[] command = response.split(" ");
        if (command[0].equals("instruction")) {
            try {
                switch (command[1]) {
                    case "loadInstruction" -> {
                        ApplicationInstruction instruction = instructionFromFile(command[2],command[3]);
                        addToAppInstructionList(instruction);
                    }
                    case "loadExperiment" -> executeExperiment(command[2]);
                    case "client_next" -> {
                        clientNextFlag = true;
                        checkAndUnlock();
                    }
                    case "server_next" -> {
                        serverNextFlag = true;
                        checkAndUnlock();
                    }
                    default -> throw new InstructionHandlingException("Invalid command " + command[1] + " for InstructionOutput");
                }

            } catch (IndexOutOfBoundsException e) {
                throw new InstructionHandlingException("Arguments missing for command " + response  + " to InstructionOutput");
            }
        }
    }

    private synchronized void checkAndUnlock() {
        if (clientNextFlag && serverNextFlag) {
            instructionLock.unlock();
            clientNextFlag = false;
            serverNextFlag = false;
        }
    }

    private ApplicationInstruction instructionFromFile(String clientInstructionPath, String serverInstructionPath) throws InstructionFileLoadingException {
        try {
            File clientFile = new File(clientInstructionPath);
            File serverFile = new File(serverInstructionPath);
            if (!clientFile.exists() || !serverFile.exists()) {
                throw new InstructionFileLoadingException("Instruction files were not found!");
            }
            String clientInstruction = Files.readString(clientFile.toPath()).replaceAll("\\s+","");
            String serverInstruction = Files.readString(serverFile.toPath()).replaceAll("\\s+","");
            return new ApplicationInstruction(clientInstruction,serverInstruction);
        } catch (IOException e) {
            throw new InstructionFileLoadingException("Instruction files could not be read",e);
        }
    }

    private void addToAppInstructionList(ApplicationInstruction instruction) throws InstructionHandlingException {
        try {
            this.getInstructionList().addAppInstruction(instruction);
        } catch (InterruptedException e) {
            throw new InstructionHandlingException("Output interrupted while waiting on instruction list");
        }
    }

    private void addToAEInstructionList(String instruction) throws InstructionHandlingException {
        try {
            this.getInstructionList().addAEInstruction(instruction);
        } catch (InterruptedException e) {
            throw new InstructionHandlingException("Output interrupted while waiting on instruction list");
        }
    }

    private void addToComputeInstructionList(String instruction) throws InstructionHandlingException {
        try {
            this.getInstructionList().addComputeInstruction(instruction);
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
            addToAppInstructionList(new ApplicationInstruction(initialInstructionString,initialInstructionString));

            for (int i = 0; i < experimentLength; i++) {
                instructionLock.waitOnNext();
                if (aeInstructions != null) {
                    addToAEInstructionList(aeInstructions.get(i));
                    Thread.sleep(WAITING_TIME);
                }
                if (computeInstructions != null) {
                    addToComputeInstructionList(computeInstructions.get(i));
                    Thread.sleep(WAITING_TIME);
                }
                String clientInstruction = clientInstructions.get(i);
                String serverInstruction = serverInstructions.get(i);
                ApplicationInstruction instruction = new ApplicationInstruction(clientInstruction, serverInstruction);
                addToAppInstructionList(instruction);
            }
        } catch (IOException | InstructionHandlingException  e) {
            e.printStackTrace();
        } catch (InterruptedException ignored) {}
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
        List<String> clientInstructions;
        List<String> serverInstructions;

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
