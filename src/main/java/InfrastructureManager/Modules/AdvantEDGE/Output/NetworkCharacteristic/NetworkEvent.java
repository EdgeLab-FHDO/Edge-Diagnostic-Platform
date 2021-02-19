package InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic;

/**
 * Class that represents a network event to an element in an AdvantEdge deployment.
 * <p>
 * A network event consists of two parts:
 * - The element affected
 * - The {@link NetworkParameters} that will determine the event
 */
public class NetworkEvent {
    private String elementName;
    private String elementType;
    private NetworkParameters netChar;

    /**
     * Creates a new NetworkEvent object based on the affected element and the network characteristics to update
     *
     * @param elementName Name of the affected element
     * @param elementType Type of the element to apply the new NW characteristics. It can be any valid element in an AdvantEdge deployment (e.g FOG,UE, etc.)
     * @param netChar     Network characteristics that will be updated
     */
    public NetworkEvent(String elementName, String elementType, NetworkParameters netChar)
    {
        this.setElementName(elementName);
        this.setElementType(elementType);
        this.setNetChar(netChar);
    }

    /**
     * Gets affected element name.
     *
     * @return the element name
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * Sets affected element name.
     *
     * @param elementName the element name
     */
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    /**
     * Gets affected element type.
     *
     * @return the element type
     */
    public String getElementType() {
        return elementType;
    }

    /**
     * Sets affected element type.
     *
     * @param elementType the element type
     */
    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    /**
     * Gets Network Characteristics to update.
     *
     * @return the Network Characteristics to update
     */
    public NetworkParameters getNetChar() {
        return netChar;
    }

    /**
     * Sets Network Characteristics to update.
     *
     * @param netChar the Network Characteristics to update
     */
    public void setNetChar(NetworkParameters netChar) {
        this.netChar = netChar;
    }
}
