package DiagnosticsClient.Load.LoadTypes;

import LoadManagement.LoadType;

public abstract class DiagnosticsLoad {

    private final LoadType type;

    public DiagnosticsLoad(LoadType type) {
        this.type = type;
    }

    public LoadType getType() {
        return type;
    }
}
