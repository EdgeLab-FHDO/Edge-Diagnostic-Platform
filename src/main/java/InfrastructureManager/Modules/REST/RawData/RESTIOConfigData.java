package InfrastructureManager.Modules.REST.RawData;

/**
 * Abstract type that represents a REST input or output in a raw form.
 */
public abstract class RESTIOConfigData {

    private final String name;
    private final String URL;

    /**
     * Constructor of the class.
     *
     * @param name Name of the IO
     * @param URL  URL defined for the IO
     */
    public RESTIOConfigData(String name, String URL) {
        this.name = name;
        this.URL = URL;
    }

    /**
     * Returns the name of this raw IO
     *
     * @return Name of this raw IO
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the defined URL of this raw IO
     *
     * @return URL of this raw IO.
     */
    public String getURL() {
        return URL;
    }
}
