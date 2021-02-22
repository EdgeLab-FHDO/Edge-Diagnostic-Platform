package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

/**
 * This interface provides a standard, well-defined manner of giving objects inside
 * the module (in this case the Scenario module) access to a shared resource.
 * <p>
 * In this particular case, provides access to the {@link Scenario} assigned to a certain module.
 * <p>
 * Having a separate interface adds a level of "information hiding" and allows objects to only access global variables in the module
 * without being able to alter the state of the module itself.
 */
interface GlobalVarAccessScenarioModule extends ImmutablePlatformModule {
    /**
     * Method to be implemented by the module to provide access to its Scenario
     *
     * @return Scenario of the module.
     */
    Scenario getScenario();
}
