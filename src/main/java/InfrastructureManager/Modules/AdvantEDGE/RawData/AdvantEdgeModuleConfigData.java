package InfrastructureManager.Modules.AdvantEDGE.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AdvantEdgeModuleConfigData extends ModuleConfigData {

    private final int port;
    private final String address;

    @JsonCreator
    public AdvantEdgeModuleConfigData(@JsonProperty("name") String name,
                                      @JsonProperty("port") int port,
                                      @JsonProperty("address") String address) {
        super(name);
        this.type = ModuleType.ADVANTEDGE;
        this.port = port;
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }
}
