package InfrastructureManager.MatchMaking;

import InfrastructureManager.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MatchMaker implements MasterInput, MasterOutput {

    private MatchMakingAlgorithm algorithm;
    private final ObjectMapper mapper;
    private String command = "";

    public MatchMaker() {
        this.algorithm = new RandomMatchMaking(); // For now
        this.mapper = new ObjectMapper();
    }

    public void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    private void assign (EdgeClient client) {
        EdgeNode node = this.algorithm.match(client);
        if (node == null) {
            System.out.println("No node to assign");
        } else {
            Master.getInstance().mapClientNode(client, node);
            command = "give_node " + client.getId();
        }
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ");
        if (command[0].equals("matchMaker")) {
            try {
                switch (command[1]){
                    case "register_client" :
                        registerClient(command[2]);
                        break;
                    case "register_node" :
                        registerNode(command[2]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for MatchMaker");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - MatchMaker");
            }
        }
    }

    private void registerClient(String clientAsString) {
        try {
            EdgeClient client = this.mapper.readValue(clientAsString, EdgeClient.class);
            Master.getInstance().addClient(client);
            assign(client); //Perform MatchMaking
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void registerNode(String nodeAsString) {
        try {
            EdgeNode node = this.mapper.readValue(nodeAsString, EdgeNode.class);
            Master.getInstance().addNode(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String read() throws Exception {
        if (command.isEmpty()) {
            throw new Exception("No command exception");
        }
        String toSend = command;
        command = "";
        return toSend;
    }
}
