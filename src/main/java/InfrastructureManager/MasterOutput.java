package InfrastructureManager;

import InfrastructureManager.MatchMaking.Exception.*;
import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class MasterOutput {
    private final String name;

    public MasterOutput() {
        this.name = null;
    }

    public MasterOutput(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void out (String response) throws Exception;
}
