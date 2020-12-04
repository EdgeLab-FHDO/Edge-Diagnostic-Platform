package InfrastructureManager;

public class EdgeClient {
    /*
    TODO: update client's requirements
    TODO: update javadoc when changed

    EdgeClient class with client's parameters (resource, id, etc)
    example to copy and paste:

    {
    "id" : "client1",
    "reqNetwork" : "5",
    "reqResource" :"10",
    "distance" : 60
    }

     */
    private final String id;
    private final long reqResource; //required computation resource
    private final long reqNetwork;  //required network bandwidth
    private final long location; //current client location, (ping will be calculated here, more info in score based match making (SBMM))

    /*
    Normal class constructor
     */
    public EdgeClient() {
        this.id = null;
        //Long.MAX to make the tracing easier since the number would be crazy when things go wrong
        this.reqResource = Long.MAX_VALUE;
        this.reqNetwork = Long.MAX_VALUE;
        this.location = Long.MAX_VALUE;
    }

    /*
    Change parameter from here for testing purpose
     */
    public EdgeClient(String id, long reqRes, long reqNet, long location) {
        this.id = id;
        this.reqResource = reqRes;
        this.reqNetwork = reqNet;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public long getReqResource() {
        return reqResource;
    }

    public long getReqNetwork() {
        return reqNetwork;
    }

    public long getLocation() {
        return location;
    }


    @Override
    public String toString() {
        return "Client{" +
                "\n  id : " + id +
                ", \n  required_resource : " + reqResource +
                ", \n  required_network : " + reqNetwork +
                ", \n  location : " + location +
                "\n";
    }
}
