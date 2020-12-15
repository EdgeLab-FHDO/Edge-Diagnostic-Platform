package InfrastructureManager;

public class EdgeClient {
    /*
    EdgeClient class with client's parameters (resource, id, etc)
    example to copy and paste: - {"id":"client1","reqNetwork":5,"reqResource":10,"location":60}
    {
    "id" : "client1",
    "reqNetwork" : "5",
    "reqResource" :"10",
    "location" : 60
    }
    name in JSON string must match the variable down here
     */
    private final String id;
    private final long reqResource; //required computation resource
    private final long reqNetwork;  //required network bandwidth
    private final long location; //current client location, (ping will be calculated here, more info in score based match making (SBMM))
    private final String message; // use this as temp variable for sending status about client, such as its disconnected reason etc
    public EdgeClient() {
        this.id = null;
        //Long.MAX to make the tracing easier since the number would be crazy when things go wrong
        this.reqResource = Long.MAX_VALUE;
        this.reqNetwork = Long.MAX_VALUE;
        this.location = Long.MAX_VALUE;
        this.message = "no message"; // to send custom command if needed (similar to job fail, job done in history)
    }
    public EdgeClient(String id, long reqRes, long reqNet, long location, String message) {
        this.id = id;
        this.reqResource = reqRes;
        this.reqNetwork = reqNet;
        this.location = location;
        this.message = message;
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

    public String getMessage() {return message; }

    @Override
    public String toString() {
        return "Client{" +
                "\n  id : " + id +
                ", \n  required_resource : " + reqResource +
                ", \n  required_network : " + reqNetwork +
                ", \n  location : " + location +
                ", \n  message : " + message +
                "\n";
    }
}
