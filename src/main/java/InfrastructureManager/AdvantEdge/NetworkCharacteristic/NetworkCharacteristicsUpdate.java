package InfrastructureManager.AdvantEdge.NetworkCharacteristic;

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
}
