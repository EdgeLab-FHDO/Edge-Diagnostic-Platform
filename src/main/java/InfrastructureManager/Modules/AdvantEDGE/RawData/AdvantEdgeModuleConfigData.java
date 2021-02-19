package InfrastructureManager.Modules.AdvantEDGE.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw data representing an AdvantEdgeModule.
 *
 * To create an advantEdge module the name, the address of the AdvantEdge deployment and the port where it
 * is exposed are needed.
 *
 * Type is automatically selected as {@link ModuleType}.ADVANTEDGE
 */
public class AdvantEdgeModuleConfigData extends ModuleConfigData {

    private final int port;
    private final String address;

    /**
     * Creates a new raw AdvantEdge module. Uses the {@link JsonProperty} annotation to extract information from
     * "name", "port" and "address" fields when deserializing.
     * @param name Name for this module
     * @param port Port where the advantEdge deployment is exposed
     * @param address Address where the advantEdge deployment is located
     */
    @JsonCreator
    public AdvantEdgeModuleConfigData(@JsonProperty("name") String name,
                                      @JsonProperty("port") int port,
                                      @JsonProperty("address") String address) {
        super(name);
        this.type = ModuleType.ADVANTEDGE;
        this.port = port;
        this.address = address;
    }

    /**
     * Getter method for the port in which the advantEdge deployment is exposed
     * @return Port where AdvantEdge is exposed
     */
    public int getPort() {
        return port;
    }

    /**
     * Getter method for the address where the advantEdge deployment is located
     * @return Address where AdvantEdge is located
     */
    public String getAddress() {
        return address;
    }
}
