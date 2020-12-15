package InfrastructureManager;

public class EdgeNode {
    /*
    TODO: update client's requirements
    TODO: update javadoc when changed
    EdgeClient class with client's parameters (resource, id, etc)

    the variable from JSON must match the global variable below
    example for copy and paste:

    {
    "id" : "node1",
    "ipAddress": "68.131.232.215 : 30968",
    "connected" : true,
    "resource" : 100,
    "network" : 100,
    "location" : 55
    }

     */
    private final String id;
    private String ipAddress;
    private boolean connected;
    private long resource; //available computation resource on node
    private long network; // available network bandwidth on node
    private long location; //node's location, for ping calculation
    private long totalResource; //Maximum amount of resource the node have
    private long totalNetwork; // maximum amount of network bandwidth the node have

    /*
   ------------------------Getter function--------------------------------
    */
    public String getId() {
        return id;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public long getNetwork() {
        return network;
    }

    public long getResource() {
        return resource;
    }

    public long getLocation() {
        return location;
    }

    public long getTotalResource() { return totalResource;}

    public long getTotalNetwork() { return totalNetwork;}

    public void setTotalNetwork( long inputTotalNetwork){
        this.totalNetwork = inputTotalNetwork;
    }

    public void setResource(long resource) {
        this.resource = resource;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setNetwork(long network) {
        this.network = network;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public void setTotalResource(long totalResource) {
        this.totalResource = totalResource;
    }
    public EdgeNode() {
        this.id = null;
        this.ipAddress = "null";
        this.connected = false;
        this.resource = Long.MAX_VALUE;
        this.network = Long.MAX_VALUE;
        this.location = Long.MAX_VALUE;
        this.totalResource = Long.MAX_VALUE;
        this.totalNetwork = Long.MAX_VALUE;

    }

    public EdgeNode(String id, String ip, boolean connected, long thisResource, long thisTotalResource, long thisNetwork, long thisTotalNetwork, long thisLocation) {
        this.id = id;
        this.ipAddress = ip;
        this.connected = connected;
        this.resource = thisResource;
        this.network = thisNetwork;
        this.location = thisLocation;
        this.totalNetwork = thisTotalNetwork;
        this.totalResource = thisTotalResource;
    }


    //TODO: edit this when add new data parameter, left and right side must be exact in order to print things out.
    @Override
    public String toString() {
        return  "EdgeNode {\n" +
                "  id : " + id + ",\n" +
                "  ipAddress : " + ipAddress + ",\n" +
                "  connected : " + connected + ",\n" +
                "  resource : " + resource + ",\n" +
                "  totalResource : " + totalResource + ",\n" +
                "  network : " + network + ",\n" +
                "  totalNetwork : " + totalNetwork + ",\n" +
                "  location : " + location + "\n}" ;

    }
}
