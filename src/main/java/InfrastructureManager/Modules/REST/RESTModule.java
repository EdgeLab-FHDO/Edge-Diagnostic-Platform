package InfrastructureManager.Modules.REST;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.REST.RawData.GETOutputConfigData;
import InfrastructureManager.Modules.REST.RawData.POSTInputConfigData;

import java.util.List;

public class RESTModule extends PlatformModule {

    public RESTModule(String name, int port, String baseURL,
                      List<POSTInputConfigData> inputDataList,
                      List<GETOutputConfigData> outputDataList) {
        super(name);
        RestServerRunner.configure("REST_SERVER",port);
        setInputs(RESTModuleConfiguration.getInputsFromData(inputDataList, baseURL, name));
        setOutputs(RESTModuleConfiguration.getOutputsFromData(outputDataList, baseURL, name));
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
