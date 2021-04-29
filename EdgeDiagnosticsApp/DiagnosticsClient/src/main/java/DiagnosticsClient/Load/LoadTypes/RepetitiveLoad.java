package DiagnosticsClient.Load.LoadTypes;

import LoadManagement.LoadType;

public class RepetitiveLoad extends DiagnosticsLoad {

    private final int times;
    private final int interval_ms;
    private long dataLength;

    public RepetitiveLoad(LoadType type, int times, int interval_ms, long dataLength_bytes) {
        super(type);
        this.times = times;
        this.interval_ms = interval_ms;
        this.dataLength = dataLength_bytes;
    }

    public int getTimes() {
        return times;
    }

    public int getInterval_ms() {
        return interval_ms;
    }

    public long getDataLength() {
        return dataLength;
    }

    public void setDataLength(long dataLength) {
        this.dataLength = dataLength;
    }
}
