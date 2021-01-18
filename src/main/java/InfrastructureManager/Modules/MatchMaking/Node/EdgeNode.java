package InfrastructureManager.Modules.MatchMaking.Node;

import java.util.Objects;

public class EdgeNode {

    private final String id;
    private String ipAddress;
    private boolean connected;
    private long resource; //available computation resource on node
    private long network; // available network bandwidth on node
    private long location; //node's location, for ping calculation
    private long totalResource; //Maximum amount of resource the node have
    private long totalNetwork; // maximum amount of network bandwidth the node have

    /*
   ------------------------Getter & setter function--------------------------------
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

    public long getTotalResource() {
        return totalResource;
    }

    public long getTotalNetwork() {
        return totalNetwork;
    }

    public void setTotalNetwork(long thisTotalComputingResource) {
        this.totalNetwork = thisTotalComputingResource;
        this.network = thisTotalComputingResource;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setTotalResource(long thisTotalNetworkBandwidth) {
        this.totalResource = thisTotalNetworkBandwidth;
        this.resource = thisTotalNetworkBandwidth;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public EdgeNode() {
        this.id = null;
        this.ipAddress = "null";
        this.connected = false;
        this.resource = 0;
        this.network = 0;
        this.totalResource = 0;
        this.totalNetwork = 0;
        this.location = 0;
    }

    public EdgeNode(String id, String ip, boolean connected, long thisTotalComputingResource, long thisTotalNetworkBandwidth, long thisLocation) {
        this.id = id;
        this.ipAddress = ip;
        this.connected = connected;
        this.resource = thisTotalComputingResource;
        this.network = thisTotalNetworkBandwidth;
        this.totalNetwork = thisTotalNetworkBandwidth;
        this.totalResource = thisTotalComputingResource;
        this.location = thisLocation;
    }

    public void updateComputingResource(Long usedComputingResource) {
        resource = resource - usedComputingResource;
    }

    public void updateNetworkBandwidth(Long usedNetworkBandwidth) {
        network = network - usedNetworkBandwidth;
    }


    //edit this when add new data parameter, left and right side must be exact in order to print things out.
    @Override
    public String toString() {
        return "EdgeNode {\n" +
                "  id : " + id + ",\n" +
                "  ipAddress : " + ipAddress + ",\n" +
                "  connected : " + connected + ",\n" +
                "  resource : " + resource + ",\n" +
                "  totalResource : " + totalResource + ",\n" +
                "  network : " + network + ",\n" +
                "  totalNetwork : " + totalNetwork + ",\n" +
                "  location : " + location + "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeNode edgeNode = (EdgeNode) o;
        return Objects.equals(id, edgeNode.id);
    }
}