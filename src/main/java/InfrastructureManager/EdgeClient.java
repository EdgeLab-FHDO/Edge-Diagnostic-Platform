package InfrastructureManager;

public class EdgeClient {
    /*
    TODO: update client's requirements
    TODO: update javadoc when changed
    EdgeClient class with client's parameters (resource, id, etc)
     */
    private String id;
    private long reqResource;

    /*
    Normal class constructor
     */
    public EdgeClient() {
        this.id = null;
        //Long.MAX to make the tracing easier since the number would be crazy when things go wrong
        this.reqResource = Long.MAX_VALUE;
    }

    /*
    Change parameter from here for testing purpose
     */
    public EdgeClient(String id, long reqRes) {
        this.id = id;
        this.reqResource = reqRes;
    }

    public String getId() {
        return id;
    }
    public long getReqResource(){
        return reqResource;
    }
}
