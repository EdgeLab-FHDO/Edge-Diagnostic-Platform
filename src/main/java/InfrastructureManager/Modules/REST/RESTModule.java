package InfrastructureManager.Modules.REST;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.REST.RawData.GETOutputConfigData;
import InfrastructureManager.Modules.REST.RawData.POSTInputConfigData;
import InfrastructureManager.Modules.REST.RawData.RESTModuleConfigData;

import java.util.List;

public class RESTModule extends PlatformModule {

    public RESTModule() {
        super();
    }

    @Override
    public void configure(ModuleConfigData data) {
        RESTModuleConfigData castedData = (RESTModuleConfigData) data;
        String name = castedData.getName();
        String baseURL = castedData.getBaseURL();
        this.setName(name);
        RestServerRunner.configure("REST_SERVER", castedData.getPort());
        setInputs(RESTModuleConfiguration.getInputsFromData(castedData.getPOSTInputs(), baseURL, name));
        setOutputs(RESTModuleConfiguration.getOutputsFromData(castedData.getGETOutputs(), baseURL, name));
    }

    private void startServerThread() {
        Thread serverThread = new Thread(RestServerRunner.getInstance(), "REST Server thread");
        serverThread.start();
    }

    @Override
    public void start() {
        startServerThread();
        super.start();
    }

    @Override
    public void stop() {
        RestServerRunner.getInstance().exit();
        super.stop();
    }
}
