package InfrastructureManager.Modules.REST;

import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.REST.Input.POSTInput;
import InfrastructureManager.Modules.REST.Output.GETOutput;
import InfrastructureManager.Modules.REST.Output.ParametrizedGETOutput;
import InfrastructureManager.Modules.REST.RawData.GETOutputConfigData;
import InfrastructureManager.Modules.REST.RawData.POSTInputConfigData;
import InfrastructureManager.Modules.REST.RawData.RESTIOConfigData;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that provides static methods to help with the configuration of a {@link RESTModule}.
 */
public class RESTModuleConfiguration {

    /**
     * Given a list of input raw data objects, creates a list of inputs for the module
     *
     * @param data    List of raw REST input data objects
     * @param baseURL Base URL of the REST module. This will be added to all routes in the rest server
     * @param module  Owner module of the generated inputs
     * @return A list of configured inputs for the REST Module
     */
    public static PlatformInput[] getInputsFromData(List<POSTInputConfigData> data, String baseURL, PlatformModule module) {
        List<PlatformInput> result = new ArrayList<>();
        String moduleName = module.getName();
        for (POSTInputConfigData inputData : data) {
            String name = createName(inputData,moduleName);
            result.add(new POSTInput(module, name,
                    baseURL + inputData.getURL(), inputData.getCommand(), inputData.getInformation()));
        }
        return result.toArray(new PlatformInput[0]);
    }

    /**
     * Given a list of output raw data objects, creates a list of outputs for the module
     *
     * @param data    List of raw REST output data objects
     * @param baseURL Base URL of the REST module. This will be added to all routes in the rest server
     * @param module  Owner module of the generated outputs
     * @return A list of configured outputs for the REST Module
     */
    public static PlatformOutput[] getOutputsFromData(List<GETOutputConfigData> data, String baseURL, PlatformModule module) {
        List<PlatformOutput> result = new ArrayList<>();
        String moduleName =  module.getName();
        for (GETOutputConfigData outputData : data) {
            String name = createName(outputData,moduleName);
            result.add(getTypedOutput(module,name, baseURL + outputData.getURL()));
        }
        return result.toArray(new PlatformOutput[0]);
    }

    /**
     * Creates the name for inputs and outputs by combining the defined name with the owner module's name
     *
     * @param data       Raw REST IO data
     * @param moduleName Owner Module name
     * @return Name of the input or output
     */
    private static String createName(RESTIOConfigData data, String moduleName) {
        return moduleName + "." + data.getName();
    }

    /**
     * Based on the presence (or absence) of a path parameter in the URI of a GET output, creates
     * and returns a different type of output.
     *
     * @param module Owner module of the output
     * @param name   Name of the output
     * @param URL    Defined URI for the output
     * @return A GET output instance (That could internally be a ParametrizedGETOutput)
     */
    private static GETOutput getTypedOutput(PlatformModule module, String name, String URL) {
        String parameter = getRESTPathParameter(URL);
        if (parameter == null) {
            return new GETOutput(module,name, URL);
        } else {
            return new ParametrizedGETOutput(module,name, URL,parameter);
        }
    }

    /**
     * For GET outputs. Search in the defined URI, if any path parameter is present (for example name in "hello/:name").
     * If so returns the parameter
     *
     * @param URL Defined URI for a GET output
     * @return The path parameter present in the URI. If no parameter is found returns null.
     */
    private static String getRESTPathParameter(String URL) {
        if (URL.matches(".*/:\\w*$")) {
            return URL.substring(URL.indexOf("/:") + 2);
        } else {
            return null;
        }
    }
}
