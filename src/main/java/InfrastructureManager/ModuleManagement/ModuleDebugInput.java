package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ModuleDebugInput extends ModuleInput {

    private final BlockingQueue<String> debugData;

    public ModuleDebugInput(PlatformModule module, String name) {
        super(module,name);
        debugData = new LinkedBlockingDeque<>();
    }

    @Override
    public String read() throws InterruptedException {
        return this.debugData.take();
    }

    public void debug(String message){
        this.store("DEBUG - " + message);
    }

    public void warn(String message){
        this.store("WARN - " + message);
    }

    public void error(String message){
        this.store("ERROR - " + message);
    }

    private void store(String message) {
        try {
            this.debugData.put("fromDebug " + message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void response(ModuleExecutionException outputException) {
        if (outputException != null) {
            this.error(outputException.getMessage());
        }
    }
}
