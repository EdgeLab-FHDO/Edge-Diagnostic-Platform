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
    "reqResource" :"10"
    }

     */
    private String id;
    private long reqResource; //required computation resource
    private long reqNetwork;  //required network bandwidth

    /*
    Normal class constructor
     */
    public EdgeClient() {
        this.id = null;
        //Long.MAX to make the tracing easier since the number would be crazy when things go wrong
        this.reqResource = Long.MAX_VALUE;
        this.reqNetwork = Long.MAX_VALUE;
    }

    /*
    Change parameter from here for testing purpose
     */
    public EdgeClient(String id, long reqRes, long reqNet) {
        this.id = id;
        this.reqResource = reqRes;
        this.reqNetwork = reqNet;
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
}
