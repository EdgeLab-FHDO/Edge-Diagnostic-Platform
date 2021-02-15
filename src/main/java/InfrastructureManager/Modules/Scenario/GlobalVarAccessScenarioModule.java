package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

public interface GlobalVarAccessScenarioModule extends ImmutablePlatformModule {
    Scenario getScenario();
}
