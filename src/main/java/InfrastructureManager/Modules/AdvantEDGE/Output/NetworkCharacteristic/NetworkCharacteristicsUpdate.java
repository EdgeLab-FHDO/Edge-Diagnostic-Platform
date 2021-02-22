package InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic;

/**
 * Class representing a Network characteristics update in advantEdge.
 *
 * Citing the wiki: "A network characteristic update event is used to apply, at runtime, new network characteristics to the selected network element. All traffic flows passing through or reaching the updated network element must be re-calculated and re-applied."
 *
 * Here the main component of a NCU (network characteristic update) is a {@link NetworkEvent}.
 * This class adds a name for the update and a type as required by advantEdge.
 */
public class NetworkCharacteristicsUpdate {
    private String name = "name";
    private String type = "NETWORK-CHARACTERISTICS-UPDATE";
    private NetworkEvent eventNetworkCharacteristicsUpdate;

    /**
     * Instantiates a new Network characteristics update.
     *
     * @param networkEvent the network event that includes the changes and element to change.
     */
    public NetworkCharacteristicsUpdate(NetworkEvent networkEvent)
    {
        setEventNetworkCharacteristicsUpdate(networkEvent);
    }

    /**
     * Gets the event of network characteristics update.
     *
     * @return Event describing update
     */
    public NetworkEvent getEventNetworkCharacteristicsUpdate() {
        return eventNetworkCharacteristicsUpdate;
    }

    /**
     * Sets event for the network characteristics update.
     *
     * @param eventNetworkCharacteristicsUpdate Event describing update
     */
    public void setEventNetworkCharacteristicsUpdate(NetworkEvent eventNetworkCharacteristicsUpdate) {
        this.eventNetworkCharacteristicsUpdate = eventNetworkCharacteristicsUpdate;
    }

    /**
     * Gets name of the update event.
     *
     * @return the name of update event
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of the update event.
     *
     * @param name the name of the update event
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets type of the update event.
     *
     * @return the type of the update event
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type of the update event.
     *
     * @param type the type of the update event
     */
    public void setType(String type) {
        this.type = type;
    }
}
