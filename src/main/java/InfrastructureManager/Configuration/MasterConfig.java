package InfrastructureManager.Configuration;

public interface MasterConfig<T> extends MasterResource<T> {
    void set (T config);
}
