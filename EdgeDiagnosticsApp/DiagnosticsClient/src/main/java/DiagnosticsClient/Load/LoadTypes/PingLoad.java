package DiagnosticsClient.Load.LoadTypes;

import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import LoadManagement.LoadType;

import java.util.concurrent.ThreadLocalRandom;

public class PingLoad extends RepetitiveLoad {

    private byte[] data;

    public PingLoad(int times, int pingInterval_ms,long dataLength) {
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
