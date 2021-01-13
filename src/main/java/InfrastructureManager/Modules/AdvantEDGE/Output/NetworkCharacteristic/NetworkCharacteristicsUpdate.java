package InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic;

public class NetworkCharacteristicsUpdate {
    private String name = "name";
    private String type = "NETWORK-CHARACTERISTICS-UPDATE";
    private NetworkEvent eventNetworkCharacteristicsUpdate;

    public NetworkCharacteristicsUpdate(NetworkEvent networkEvent)
    {
        setEventNetworkCharacteristicsUpdate(networkEvent);
    }

    public NetworkEvent getEventNetworkCharacteristicsUpdate() {
        return eventNetworkCharacteristicsUpdate;
    }

    public void setEventNetworkCharacteristicsUpdate(NetworkEvent eventNetworkCharacteristicsUpdate) {
        this.eventNetworkCharacteristicsUpdate = eventNetworkCharacteristicsUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
