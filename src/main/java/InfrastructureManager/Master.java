package InfrastructureManager;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Configuration.MasterConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Master Class of the Infrastructure Manager, singleton class
 */
public class Master {

    private final List<Runner> runnerList;
    private final List<EdgeNode> nodeList;
    private final List<EdgeClient> clientList;

    private Thread mainThread;
    private Thread restThread;

    private static Master instance = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructor of the class
     * Gets the master configured according to the config file.
     */
    private Master() {
        MasterConfigurator configurator = new MasterConfigurator();
        runnerList = configurator.getRunners();
        clientList = new ArrayList<>();
        nodeList = new ArrayList<>();
        /* Temporal made up nodes for testing */
        /* TODO: this is outdated, need to update when we are done defining node's characteristic
        availableNodes.add(new EdgeNode("node1", "192.168.0.1",true));
        availableNodes.add(new EdgeNode("node2", "192.168.0.2",true));
        availableNodes.add(new EdgeNode("node3", "192.168.0.3",true));
        availableNodes.add(new EdgeNode("node4", "192.168.0.4",true));
        availableNodes.add(new EdgeNode("node5", "192.168.0.5",true));
        /*---------------------------------------------------------*/
    }


    public String execute(String fromInput, CommandSet commands) {
        return commands.getResponse(fromInput);
    }

    /**
     * Method for exiting the program, by exiting each running runner
     */
    public void exitAll() {
        for (Runner runner : runnerList) {
            if (runner.isRunning()) {
                runner.exit();
            }
        }
    }

    /**
     * Method for starting the main runner thread, declared in the config file
     */
    public void startMainRunner() {
        for (Runner runner : runnerList) {
            if (runner.getName().equals("Runner_console_in")) {
                mainThread = new Thread(runner, "MainRunner");
                mainThread.start();
                break;
            }
        }
    }

    /**
     * Method for starting the REST runner thread, declared in the config file
     */
    public void startRunnerThread(String name) {
        for (Runner runner : runnerList) {
            if (runner.getName().equals(name)) {
                if (name.equals("RestServer") && restThread == null) {
                    restThread = new Thread(runner, "RestRunner");
                    restThread.start();
                    break;
                }
                new Thread(runner, name).start();
                break;
            }
        }
    }

    public Thread getMainThread() {
        return mainThread;
    }

    /**
     * Method that given a certain Scenario, runs the corresponding ScenarioRunner and adds
     * it to the list of running Runners. If called on a running ScenarioRunner, it will restart
     * it
     *
     * @param scenario Scenario to be run
     */
    public void runScenario(Scenario scenario, long startTime) {
        try {
            ScenarioRunner scenarioRunner = getRunner(scenario);
            if (!scenarioRunner.isRunning()) { //If running then leave it running
                scenarioRunner.setScenario(scenario, startTime);
                new Thread(scenarioRunner).start(); // Run the scenario in another thread
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop a running ScenarioRunner, given the scenario that is running
     *
     * @param scenario Running scenario to be stopped
     */
    public void stopScenario(Scenario scenario) {
        try {
            ScenarioRunner scenarioRunner = getRunner(scenario);
            if (scenarioRunner.isRunning()) {
                scenarioRunner.exit();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to pause a running ScenarioRunner, given the scenario that is running
     *
     * @param scenario Running scenario to be paused
     */
    public void pauseScenario(Scenario scenario) {
        try {
            ScenarioRunner scenarioRunner = getRunner(scenario);
            if (scenarioRunner.isRunning()) {
                scenarioRunner.pause();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to resume a paused ScenarioRunner, given its scenario
     *
     * @param scenario Paused scenario to be resumed
     */
    public void resumeScenario(Scenario scenario) {
        try {
            ScenarioRunner scenarioRunner = getRunner(scenario);
            if (scenarioRunner.isPaused()) {
                scenarioRunner.resume();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for finding a ScenarioRunner in the Runner list, based on the scenario is
     * supposed to run according to the configuration file
     *
     * @param scenario Scenario for which to find the ScenarioRunner
     * @return ScenarioRunner that will run the given scenario
     * @throws IllegalArgumentException If there is no scenarioRunner configured to run the
     *                                  given scenario
     */
    private ScenarioRunner getRunner(Scenario scenario) throws IllegalArgumentException {
        ScenarioRunner scenarioRunner;
        for (Runner runner : runnerList) {
            try {
                scenarioRunner = (ScenarioRunner) runner;
                if (scenarioRunner.getScenarioName().equalsIgnoreCase(scenario.getName())) {
                    return scenarioRunner;
                }
            } catch (Exception e) {
                continue;
            }
        }
        throw new IllegalArgumentException("There is no runner configured for the given scenario");
    }

    public List<EdgeNode> getNodeList() {
        return nodeList;
    }

    /*
    Add node to a list (nodeList)
     */
    public void addNode(EdgeNode node) {
        if (checkDuplicateNodeInList(node)) {
            logger.warn("Node [{}] already exist, skip ", node.getId());
        } else {
            this.nodeList.add(node);
        }
    }


    /**
     * Update node in the list (nodeList) when things change
     * @author Zero
     * @param oldNodeID
     * @param newNode
     * @throws Exception
     */
    /*

     */
    public void updateNode(String oldNodeID, EdgeNode newNode) throws Exception {
        //get updating node location
        Integer thisNodeLocation = null;
        logger.info("oldNodeID = {}\n newNode: {}", oldNodeID, newNode.toString());

        for (EdgeNode oldNode : this.nodeList) {
            logger.info("checking node: {}", oldNode.getId());
            if (oldNode.getId().equalsIgnoreCase(oldNodeID)) {
                logger.info("nodeID matched with new node [{}], leaving\n ", newNode.getId());
                thisNodeLocation = this.nodeList.indexOf(oldNode);
                //get out when found, no need for further exploration
                break;
            }
        }
        //updating process
        if (thisNodeLocation == null) {
            logger.error("new node ID does not match with any node's ID in the system.\n ");
            throw new Exception();
        }
        this.nodeList.set(thisNodeLocation, newNode);
    }

    /*
    Add client to a list (registeredClients)
     */
    public void addClient(EdgeClient thisClient) {

        //Check whether this client has already been registered
        if (checkDuplicateClientInList(thisClient)) {
            logger.warn("Client: [{}] already exist, skip ", thisClient.getId());
        }
        //Add new client to the list
        else {
            this.clientList.add(thisClient);
        }

    }

    /**
     * Update client in our clientList when things change
     * @author Zero
     * @param oldClientID
     * @param newClient
     * @throws Exception
     */

    public void updateClient(String oldClientID, EdgeClient newClient) throws Exception {
        //get updating client location
        Integer clientLocation = null;
        logger.info("oldClientID = {}\n newClient: {}", oldClientID, newClient.toString());

        for (EdgeClient client : this.clientList) {
            logger.info("checking client: {}", client.getId());
            if (client.getId().equalsIgnoreCase(oldClientID)) {
                logger.info("nodeID matched with new node [{}], leaving\n ", newClient.getId());
                clientLocation = this.clientList.indexOf(client);
                //get out when found, no need for further exploration
                break;
            }
        }
        //updating process
        if (clientLocation == null) {
            logger.error("new node ID does not match with any node's ID in the system.\n ");
            throw new Exception();
        }
        this.clientList.set(clientLocation, newClient);
    }

    /*
    Find and return client whose ID match with clientID by iterating list of registeredClient
     */
    public EdgeClient getClientByID(String clientID) throws Exception {
        for (EdgeClient client : this.clientList) {
            if (client.getId().equals(clientID)) {
                return client;
            }
        }
        throw new Exception("No client found");
    }


    public EdgeNode getNodeByID(String nodeID) throws Exception {
        for (EdgeNode node : this.nodeList) {
            if (node.getId().equals(nodeID)) {
                return node;
            }
        }
        throw new Exception("No node found");
    }

    /**
     * Check for duplication of node within the list
     * @author Zero
     * @param thisNode
     * @return true if duplicated, false if it's unique (not in list yet)
     */
    private boolean checkDuplicateNodeInList(EdgeNode thisNode) {

        //If there is a node that have the same name with thisNode in parameter, it's a duplicated
        for (EdgeNode node : this.nodeList) {
            if (node.getId().equals(thisNode.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check for duplication of client within the list
     * @author Zero
     * @param thisClient
     * @return true if duplicated, false if it's unique (not in list yet)
     */
    private boolean checkDuplicateClientInList(EdgeClient thisClient) {

        //If there is a node that have the same name with thisNode in parameter, it's a duplicated
        for (EdgeClient client : this.clientList) {
            if (client.getId().equals(thisClient.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Singleton method for getting the only instance of the class
     *
     * @return The instance of the Master Class
     */
    public static Master getInstance() {
        if (instance == null) {
            instance = new Master();
        }
        return instance;
    }


    public static void main(String[] args) {
        Master.getInstance().startMainRunner();
        try {
            Master.getInstance().getMainThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
