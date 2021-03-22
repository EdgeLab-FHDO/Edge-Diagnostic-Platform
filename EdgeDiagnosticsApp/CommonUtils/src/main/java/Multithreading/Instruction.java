package Multithreading;

import LoadManagement.BasicLoadManager.ConnectionType;
import LoadManagement.LoadType;

public interface Instruction {
    LoadType getLoadType();
    ConnectionType getConnectionType();
}
