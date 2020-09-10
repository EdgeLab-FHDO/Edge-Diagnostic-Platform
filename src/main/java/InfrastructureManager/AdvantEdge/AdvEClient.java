package InfrastructureManager.AdvantEdge;

import InfrastructureManager.MasterOutput;

public class AdvEClient implements MasterOutput {

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ");
        if (command[0].equals("advantEdge")) { //The commands must come like "advantEdge command"
            try {
                switch (command[1]) {
                    case "create" :
                        createAEScenario(command[2], command[3]);
                        break;
                    case "deploy" :
                        deployAEScenario(command[2]);
                        break;
                    case "terminate" :
                        terminateAEScenario();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for AdvEClient");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - AdvEClient");
            }
        }
    }

    private void createAEScenario(String name, String pathToJSON) {
        String requestPath = "/scenarios/" + name;
        System.out.println("POST to " + requestPath + " with body from " + pathToJSON);
    }

    private void deployAEScenario(String name) {
        String requestPath = "/active/" + name;
        System.out.println("POST to " + requestPath);
    }

    private void terminateAEScenario() {
        //TODO: Check the active scenario first
        String requestPath = "/active";
        System.out.println("DELETE to " + requestPath);
    }
}
