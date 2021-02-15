package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

interface GlobalVarAccessScenarioModule extends ImmutablePlatformModule {
    Scenario getScenario();
}
