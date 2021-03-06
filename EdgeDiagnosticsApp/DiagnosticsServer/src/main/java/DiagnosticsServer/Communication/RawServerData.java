package DiagnosticsServer.Communication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;

public class RawServerData {
    private final String id;
    private final String ipAddress;

    @JsonCreator
    public RawServerData(String id, String ipAddress) {
        this.id = id;
        this.ipAddress = ipAddress;
    }

    @JsonGetter
    public String getId() {
        return id;
    }

    @JsonGetter
    public String getIpAddress() {
        return ipAddress;
    }
}
