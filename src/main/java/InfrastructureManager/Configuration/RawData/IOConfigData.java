package InfrastructureManager.Configuration.RawData;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@IOType", defaultImpl = IOConfigData.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CustomCommandIO.class, name = "CustomCommand")
})
public class IOConfigData {

    protected final String type;
    protected final String name;
    protected final String address;
    protected final int port;

    public IOConfigData() {
        this.type = null;
        this.name = null;
        this.address = null;
        this.port = 0;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }
}
