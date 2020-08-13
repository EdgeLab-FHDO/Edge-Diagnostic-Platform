package InfrastructureManager;

import java.util.Map;

public class MasterConfigurationData {
    private String inputSource;
    private String outputSource;
    private Map<String, String> commands;

    public MasterConfigurationData() {}

    public String getInputSource() {
        return inputSource;
    }

    public String getOutputSource() {
        return outputSource;
    }

    public Map<String, String> getCommands() {
        return commands;
    }

}
