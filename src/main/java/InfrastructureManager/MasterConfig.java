package InfrastructureManager;

public interface MasterConfig<T> extends MasterResource<T> {
    void set (T config);
}
