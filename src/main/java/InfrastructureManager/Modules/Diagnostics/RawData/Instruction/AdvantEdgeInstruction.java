package InfrastructureManager.Modules.Diagnostics.RawData.Instruction;

import java.util.HashMap;
import java.util.Map;

public class AdvantEdgeInstruction {

    private final static Map<String, Map<String,Integer>> AE_DEFAULT_VALUES;
    private String scenarioName;
    private String elementName;
    private String elementType;
    private Map<String,Integer> values;

    static {
        AE_DEFAULT_VALUES = new HashMap<>();
        String[] types = {"SCENARIO","ZONE","OPERATOR","POA","EDGE","FOG","DISTANT CLOUD","UE"};
        for (String type : types) {
            AE_DEFAULT_VALUES.put(type, getValues(type));
        }
    }
    private static Map<String, Integer> getValues(String elementType) {
        Map<String,Integer> result = new HashMap<>();
        String[] valueNames = {"latency", "latencyVariation", "packetLoss", "throughputDl", "throughputUl"};
        int[] values = switch (elementType) {
            case "SCENARIO" -> new int[]{50,10,0,1000,1000};
            case "ZONE" -> new int[]{5,1,0,1000,1000};
            case "OPERATOR" -> new int[]{6,2,0,1000,1000};
            case "POA" -> new int[]{1,1,0,1000,1000};
            default -> new int[] {0,0,0,1000,1000};
        };
        for (int i = 0; i < valueNames.length; i++) {
            result.put(valueNames[i], values[i]);
        }
        return result;
    }

    public AdvantEdgeInstruction(String scenarioName,String elementName, String elementType) {
        this.scenarioName = scenarioName;
        this.elementName = elementName;
        this.elementType = elementType;
        this.values = AE_DEFAULT_VALUES.get(elementType);
    }

    public void changeValue(String valueName, int value) {
        this.values.replace(valueName,value);
    }

    @Override
    public String toString() {
        return "sandbox-" + scenarioName + " " +
                elementName + " " +
                elementType + " " +
                values.get("throughputDl") + " " +
                values.get("throughputUl") + " " +
                values.get("latency") + " " +
                values.get("latencyVariation") + " " +
                values.get("packetLoss");
    }
}
