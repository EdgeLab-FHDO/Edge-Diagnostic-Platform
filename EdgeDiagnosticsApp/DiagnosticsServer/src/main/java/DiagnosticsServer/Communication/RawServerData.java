package DiagnosticsServer.Communication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

public class RawServerData {

    @JsonView(ServerViews.HeartBeatView.class)
    private final String id;
    @JsonView(ServerViews.RegisterView.class)
    private final String ipAddress;
    //@JsonView(ServerViews.RegisterView.class)
    @JsonIgnore
    private final int heartBeatInterval;

    @JsonCreator
    public RawServerData(String id, String ipAddress, int heartBeatInterval) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.heartBeatInterval = heartBeatInterval;
    }

    public String getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getHeartBeatInterval() {
        return heartBeatInterval;
    }
}
