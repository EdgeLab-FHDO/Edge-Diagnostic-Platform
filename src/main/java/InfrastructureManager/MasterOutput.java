package InfrastructureManager;

public abstract class MasterOutput {
    private String name;

    public MasterOutput() {
        this.name = null;
    }

    public MasterOutput(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void out (String response) throws IllegalArgumentException;
}
