package InfrastructureManager.Modules.MatchMaking.Client;

import java.util.Objects;

public class EdgeClient {

    private final String id;
    private long reqResource; //required computation resource
    private long reqNetwork;  //required network bandwidth
    private long location; //current client location, (ping will be calculated here, more info in score based match making (SBMM))
    private final String message; // use this as temp variable for sending status about client, such as its disconnected reason etc
    private EdgeClientHistory clientHistory;

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
        this.clientHistory = new EdgeClientHistory(id);
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


    public void setClientHistory(EdgeClientHistory clientHistory) {
        this.clientHistory = clientHistory;
    }

    public void setReqResource(long reqResource) {
        this.reqResource = reqResource;
    }

    public void setReqNetwork(long reqNetwork) {
        this.reqNetwork = reqNetwork;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public EdgeClientHistory getClientHistory(){return clientHistory;}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeClient client = (EdgeClient) o;
        return Objects.equals(id, client.id);
    }
}
