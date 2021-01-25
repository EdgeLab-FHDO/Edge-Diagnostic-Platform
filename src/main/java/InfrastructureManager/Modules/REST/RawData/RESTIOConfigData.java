package InfrastructureManager.Modules.REST.RawData;

public abstract class RESTIOConfigData {

    private final String name;
    private final String URL;

    public RESTIOConfigData(String name, String URL) {
        this.name = name;
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }
}
