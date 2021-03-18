package DiagnosticsClient.Load.LoadTypes;

import LoadManagement.LoadType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PingLoad.class, name = "PING"),
        @JsonSubTypes.Type(value = FileLoad.class, name = "FILE")
})
public abstract class DiagnosticsLoad {

    @JsonIgnore
    private final LoadType type;

    public DiagnosticsLoad(LoadType type) {
        this.type = type;
    }

    public LoadType getType() {
        return type;
    }
}
