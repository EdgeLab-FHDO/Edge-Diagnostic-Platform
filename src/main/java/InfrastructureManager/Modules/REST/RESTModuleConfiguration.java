package InfrastructureManager.Modules.REST;

import InfrastructureManager.ModuleManagement.ModuleInput;
import InfrastructureManager.ModuleManagement.ModuleOutput;
import InfrastructureManager.Modules.REST.Input.POSTInput;
import InfrastructureManager.Modules.REST.Output.GETOutput;
import InfrastructureManager.Modules.REST.Output.ParametrizedGETOutput;
import InfrastructureManager.Modules.REST.RawData.GETOutputConfigData;
import InfrastructureManager.Modules.REST.RawData.POSTInputConfigData;
import InfrastructureManager.Modules.REST.RawData.RESTIOConfigData;

import java.util.ArrayList;
import java.util.List;

public class RESTModuleConfiguration {

    public static ModuleInput[] getInputsFromData(List<POSTInputConfigData> data, String baseURL, String moduleName) {
        List<ModuleInput> result = new ArrayList<>();
        for (POSTInputConfigData inputData : data) {
            String name = createName(inputData,moduleName);
            result.add(new POSTInput(name,baseURL + inputData.getURL(),
                    inputData.getCommand(),inputData.getInformation()));
        }
        return result.toArray(new ModuleInput[0]);
    }

    public static ModuleOutput[] getOutputsFromData(List<GETOutputConfigData> data, String baseURL, String moduleName) {
        List<ModuleOutput> result = new ArrayList<>();
        for (GETOutputConfigData outputData : data) {
            String name = createName(outputData,moduleName);
            result.add(getTypedOutput(name, baseURL + outputData.getURL()));
        }
        return result.toArray(new ModuleOutput[0]);
    }

    private static String createName(RESTIOConfigData data, String moduleName) {
        return moduleName + "." + data.getName();
    }

    private static GETOutput getTypedOutput(String name, String URL) {
        String parameter = getRESTPathParameter(URL);
        if (parameter == null) {
            return new GETOutput(name, URL);
        } else {
            return new ParametrizedGETOutput(name, URL,parameter);
        }
    }

    private static String getRESTPathParameter(String URL) {
        if (URL.matches(".*/:\\w*$")) {
            return URL.substring(URL.indexOf("/:") + 2);
        } else {
            return null;
        }
    }
}
