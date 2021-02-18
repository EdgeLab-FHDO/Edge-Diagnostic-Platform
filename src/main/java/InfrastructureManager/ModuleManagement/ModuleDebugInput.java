package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.PlatformObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Built in input in all platform modules. Allows all the objects inside a module to log messages of three levels:
 *  - Debug
 *  - Warn
 *  - Error
 *
 * Acts like a normal platform input in the sense that can be connected with any output (for example console, or fileOutput) and
 * will return the queued messages when read. It does so by adding the logging level in front of every message in the following way:
 *
 * "LOGGING_LEVEL - MESSAGE"
 *
 * Internally is implemented with a blocking FIFO queue that stores the messages and dumps them everytime the input is read. If
 * there is no messages, the input will block until there is one.
 *
 * When reading the input, the predefined command "fromDebug" is present in every reading. This should be used to create the connections to it.
 */
public class ModuleDebugInput extends PlatformObject implements PlatformInput {

    private final BlockingQueue<String> debugData;

    /**
     * Constructor of the class. Creates a new ModuleDebugInput object
     * @param module Owner module of this debug input
     * @param name Name of the input (generally hardcoded to "MODULE_NAME.debug")
     */
    public ModuleDebugInput(PlatformModule module, String name) {
        super(module,name);
        debugData = new LinkedBlockingDeque<>();
    }

    /**
     * Implementation of the {@link PlatformInput} interface. Returns the value on top of the queue. Blocks if no value available
     * @return The value on the top of the FIFO queue
     * @throws InterruptedException If the thread is interrupted while the input is blocked waiting for a value to be put in the queue.
     */
    @Override
    public String read() throws InterruptedException {
        return this.debugData.take();
    }

    /**
     * Store a debug level message in the logging queue
     * @param message Message to be stored
     */
    public void debug(String message){
        this.store("DEBUG - " + message);
    }

    /**
     * Store a warning level message in the logging queue
     * @param message Message to be stored
     */
    public void warn(String message){
        this.store("WARN - " + message);
    }

    /**
     * Store an error level (highest) message in the logging queue
     * @param message Message to be stored
     */
    public void error(String message){
        this.store("ERROR - " + message);
    }

    /**
     * Abstraction of the public storing methods. Puts a message into the blocking queue preceded by the "fromDebug" command.
     * This pre-defined command should be used when creating connections for this input
     * @param message Message to be stored
     */
    private void store(String message) {
        try {
            this.debugData.put("fromDebug " + message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implementation of the {@link PlatformInput} interface. Deals with exceptions coming from the whole execution process.
     * In this case, creates an error level message containing the exception's message and stores it into the queue.
     * @param outputException Exception coming from processing or output operations.
     */
    @Override
    public void response(ModuleExecutionException outputException) {
        if (outputException != null) {
            this.error(outputException.getMessage());
        }
    }
}
