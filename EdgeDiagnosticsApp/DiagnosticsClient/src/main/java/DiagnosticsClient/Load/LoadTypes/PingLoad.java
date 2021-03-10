package DiagnosticsClient.Load.LoadTypes;

import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import LoadManagement.LoadType;

import java.util.concurrent.ThreadLocalRandom;

public class PingLoad extends DiagnosticsLoad {

    private byte[] data;
    private final int times;
    private final int pingInterval_ms;

    public PingLoad(int dataLength, int times, int pingInterval_ms) {
        super(LoadType.PING);
        this.times = times;
        this.pingInterval_ms = pingInterval_ms;
        fillData(dataLength);
    }

    private void fillData(int dataLength) {
        this.data = new byte[dataLength];
        ThreadLocalRandom.current().nextBytes(this.data);
    }

    public byte[] getData() {
        return data;
    }


    public int getTimes() {
        return times;
    }

    public int getPingInterval_ms() {
        return pingInterval_ms;
    }
}
