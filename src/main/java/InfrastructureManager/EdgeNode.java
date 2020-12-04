package InfrastructureManager;

public class EdgeNode {
    /*
    TODO: update client's requirements
    TODO: update javadoc when changed
    EdgeClient class with client's parameters (resource, id, etc)

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
    private final String ipAddress;
    private final boolean connected;
    private final long resource; //available computation resource on node
    private final long network; // available network bandwidth on node
    private final long location; //node's location, for ping calculation

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


    /*
    ------------------------Constructors --------------------------------
     */
    public EdgeNode() {
        this.id = null;
        this.ipAddress = null;
        this.connected = false;
        this.resource = Long.MAX_VALUE;
        this.network = Long.MAX_VALUE;
        this.location = Long.MAX_VALUE;
    }

    public EdgeNode(String id, String ip, boolean connected, long thisResource, long thisNetwork, long thisLocation) {
        this.id = id;
        this.ipAddress = ip;
        this.connected = connected;
        this.resource = thisResource;
        this.network = thisNetwork;
        this.location = thisLocation;
    }


    //TODO: edit this to accommodate new data parameter. Or just use pretty print tbh :))
    @Override
    public String toString() {
        return "EdgeNode{\n" +
                "  id : " + id + ",\n" +
                "  ipAddress : " + ipAddress + ",\n" +
                "  connected : " + connected + ",\n" +
                "  resource : " + resource + ",\n" +
                "  network : " + network + ",\n" +
                "  location : " + location + "\n}" ;
    }
}
