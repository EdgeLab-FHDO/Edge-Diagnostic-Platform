package InfrastructureManager;

import java.util.Map;
// Este es el objeto que va a estar igualito en el JSON
public class MasterConfigurationData {
    private String inputSource;
    private String outputSource;
    private Map<String, String> commands;

    public String getInputSource() {
        return inputSource;
    }

    public void setInputSource(String inputSource) {
        this.inputSource = inputSource;
    }

    public String getOutputSource() {
        return outputSource;
    }

    public void setOutputSource(String outputSource) {
        this.outputSource = outputSource;
    }

    public Map<String, String> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, String> commands) {
        this.commands = commands;
    }
}
