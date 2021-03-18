package InfrastructureManager.Modules.REST;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.REST.Exception.Server.ServerNotConfiguredException;
import InfrastructureManager.Modules.REST.Output.GETOutput;
import InfrastructureManager.Modules.REST.RawData.RESTModuleConfigData;

/**
 * {@link PlatformModule} that adds REST functionality to the platform.
 * <p>
 * This module allows the platform to have a global access point to communicate with external entities (apps, nodes).
 * It provides the functionality of creating and defining a REST API, which is entirely customizable from configuration of the platform.
 * <p>
 * In this module, contrary to others, Inputs and outputs modules are not named in a hardcoded way. This is important to remember while configuring the platform to create the connections.
 * <p>
 * POST and GET IOs are named according to their own defined name, prefixed by the module name.
 *
 * @see <a href="https://github.com/EdgeLab-FHDO/Edge-Diagnostic-Platform/wiki/REST-Module">Wiki Entry</a>
 */
public class RESTModule extends PlatformModule {

    /**
     * Creates a new REST module.
     */
    public RESTModule() {
        super();
    }

    /**
     * Based on raw module data, configures the module to use it. In this case, extracts the name, baseURL and
     * raw input and output data and creates the input and outputs with their predefined names.
     * @param data Raw module data.
     */
    @Override
    public void configure(ModuleConfigData data) {
        RESTModuleConfigData castedData = (RESTModuleConfigData) data;
        String name = castedData.getName();
        String baseURL = castedData.getBaseURL();
        this.setName(name);
        RestServerRunner.configure(this,"REST_SERVER", castedData.getPort());
        setInputs(RESTModuleConfiguration.getInputsFromData(castedData.getPOSTInputs(), baseURL, this));
        setOutputs(RESTModuleConfiguration.getOutputsFromData(castedData.getGETOutputs(), baseURL, this));
    }

    /**
     * Starts a thread in which the built in {@link RestServerRunner} runs.
     */
    private void startServerThread() {
        try {
            Thread serverThread = new Thread(RestServerRunner.getInstance(), "REST Server thread");
            serverThread.start();
        } catch (ServerNotConfiguredException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Starts the module and additionally starts the REST server.
     */
    @Override
    public void start() {
        startServerThread();
        this.getOutputs().forEach( o -> {
            GETOutput output = (GETOutput) o;
            output.activate();
        });
        super.start();
    }

    /**
     * Stops the REST server and then stops the module.
     */
    @Override
    public void stop() {
        try {
            RestServerRunner.getInstance().exit();
        } catch (ServerNotConfiguredException ignored) {}
        super.stop();
    }
}
