package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class PlatformModule extends ImmutablePlatformModule {

    protected PlatformModule() {
        super();
    }

    public abstract void configure(ModuleConfigData data);

    public void setName(String name) {
        this.name = name;
    }

    protected void setInputs(ModuleInput... inputs) {
        List<ModuleInput> temporalList = new ArrayList<>(Arrays.asList(inputs));
        this.debugInput = new ModuleDebugInput(this,name + ".debug");
        temporalList.add(this.debugInput);
        temporalList.forEach(i -> i.setRunner(new Runner(i.getName(), i)));
        this.getInputs().addAll(temporalList); //Append them to the list
    }

    protected void setOutputs(ModuleOutput... outputs) {
        this.getOutputs().addAll(Arrays.asList(outputs));
    }

    public void addConnection(String inputName, Connection connection) throws IncorrectInputException {
        if (hasInput(inputName)) {
            Map<String, List<Connection>> inputConnections = this.getInputConnections();
            if (inputConnections.containsKey(inputName)) {
                inputConnections.get(inputName).add(connection);
            } else {
                List<Connection> list = new ArrayList<>();
                list.add(connection);
                inputConnections.put(inputName,list);
            }
        } else {
            throw new IncorrectInputException("Input not defined in module!");
        }
    }
}
