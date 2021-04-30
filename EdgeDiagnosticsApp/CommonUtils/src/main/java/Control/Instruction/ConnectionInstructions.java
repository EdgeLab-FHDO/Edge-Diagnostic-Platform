package Control.Instruction;

import LoadManagement.BasicLoadManager.ConnectionType;

public abstract class ConnectionInstructions {
    private final ConnectionType type;

    public ConnectionInstructions(ConnectionType type) {
        this.type = type;
    }

    public ConnectionType getType() {
        return type;
    }
}
