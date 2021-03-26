package InfrastructureManager.Modules.MatchMaking.Client;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModuleObject;
import InfrastructureManager.PlatformObject;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class EdgeClient extends MatchMakingModuleObject {

    private final String id;
    private long reqResource; //required computation resource
    private long reqNetwork;  //required network bandwidth
    private long location; //current client location, (ping will be calculated here, more info in score based match making (SBMM))
    private final String message; // use this as temp variable for sending status about client, such as its disconnected reason etc
    private long heartBeatInterval; //period between each heartbeat signal, time in millisecond
    private boolean online; //true = online, false = offline

    private EdgeClientHistory clientHistory;

    @JsonCreator
    public EdgeClient(@JacksonInject ImmutablePlatformModule ownerModule) {
        super(ownerModule);
        this.id = null;
        //Long.MAX to make the tracing easier since the number would be crazy when things go wrong
        this.reqResource = Long.MAX_VALUE;
        this.reqNetwork = Long.MAX_VALUE;
        this.location = Long.MAX_VALUE;
        this.message = "no message"; // to send custom command if needed (similar to job fail, job done in history)
        this.heartBeatInterval = 0;
        this.online = true;
    }

    public EdgeClient(@JacksonInject ImmutablePlatformModule ownerModule, String id, long reqRes, long reqNet, long location, String message, long thisHeartBeatInterval, boolean thisOnline) {
        super(ownerModule);
        this.id = id;
        this.reqResource = reqRes;
        this.reqNetwork = reqNet;
        this.location = location;
        this.message = message;
        this.clientHistory = new EdgeClientHistory(ownerModule, id);
        this.heartBeatInterval = thisHeartBeatInterval;
        this.online = thisOnline;

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

    public String getMessage() {
        return message;
    }

    public long getHeartBeatInterval() {
        return heartBeatInterval;
    }

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

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean thisOnline) {
        this.online = thisOnline;
    }

    public EdgeClientHistory getClientHistory() {
        return clientHistory;
    }

    @Override
    public String toString() {
        return "Client{" +
                "\n  id : " + id +
                ", \n  required_resource : " + reqResource +
                ", \n  required_network : " + reqNetwork +
                ", \n  location : " + location +
                ", \n  message : " + message +
                ", \n  heartBeatInterval : " + heartBeatInterval +
                ", \n  online : " + online +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeClient client = (EdgeClient) o;
        return Objects.equals(id, client.id);
    }
}
