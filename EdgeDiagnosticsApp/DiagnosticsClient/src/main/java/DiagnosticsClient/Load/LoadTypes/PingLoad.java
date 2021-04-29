package DiagnosticsClient.Load.LoadTypes;

import LoadManagement.LoadType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.ThreadLocalRandom;

public class PingLoad extends RepetitiveLoad {

    @JsonIgnore
    private byte[] data;

    @JsonCreator
    public PingLoad(@JsonProperty("times") int times,
                    @JsonProperty("interval") int pingInterval_ms,
                    @JsonProperty("dataLength") long dataLength) {
        super(LoadType.PING,times,pingInterval_ms,dataLength);
        fillData(dataLength);
    }

    private void fillData(long dataLength) {
        this.data = new byte[(int) dataLength];
        ThreadLocalRandom.current().nextBytes(this.data);
    }

    public byte[] getData() {
        return data;
    }

}
