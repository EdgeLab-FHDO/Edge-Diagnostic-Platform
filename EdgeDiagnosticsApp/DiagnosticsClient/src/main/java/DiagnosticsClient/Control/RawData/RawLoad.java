package DiagnosticsClient.Control.RawData;

import LoadManagement.LoadType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RawLoad {
    private final LoadType loadType;
    private final int repetitions;
    private final int interval;

    @JsonCreator
    public RawLoad(@JsonProperty("loadType") LoadType loadType,
                       @JsonProperty("repetitions") int repetitions,
                       @JsonProperty("interval") int interval) {
        this.loadType = loadType;
        this.repetitions = repetitions;
        this.interval = interval;
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public int getInterval() {
        return interval;
    }
}
