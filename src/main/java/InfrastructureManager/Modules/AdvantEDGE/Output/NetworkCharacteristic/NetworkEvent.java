package InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic;

public class NetworkEvent {
    private String elementName;
    private String elementType;
    private NetworkParameters netChar;

    public NetworkEvent(String elementName, String elementType, NetworkParameters netChar)
    {
        this.setElementName(elementName);
        this.setElementType(elementType);
        this.setNetChar(netChar);
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public NetworkParameters getNetChar() {
        return netChar;
    }

    public void setNetChar(NetworkParameters netChar) {
        this.netChar = netChar;
    }
}
