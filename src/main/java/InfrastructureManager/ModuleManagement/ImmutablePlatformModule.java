package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.List;
import java.util.Map;

public interface ImmutablePlatformModule {
    void configure(ModuleConfigData data);

    List<PlatformInput> getInputs();

    List<PlatformOutput> getOutputs();

    String getName();

    PlatformModule.ModuleState getState();

    Map<String, List<Connection>> getInputConnections();

    ModuleDebugInput getDebugInput();
}
