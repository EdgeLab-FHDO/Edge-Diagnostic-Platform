package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

/**
 * Interface that defines an input for the Edge Diagnostics Platform.
 *
 * Inputs are understood to be everything that can provide data to the system.
 * This data is normally in the forms of commands
 *
 * Classes that act as an input for the platform should not only implement this interface but also
 * extend the {@link InfrastructureManager.PlatformObject} class (or one of its module specific subclasses)
 */
public interface PlatformInput {

    /**
     * Gives the name defined for this input. Normally is preceded by the owner module's name
     * @return Name of this input
     */
    String getName();

    /**
     * Basic functionality of a platform input. When called, gives the platform a command to process.
     * @return A command (as a String) for the platform to process
     * @throws InterruptedException if the thread is interrupted while blocked waiting for a value
     */
    String read() throws InterruptedException;

    /**
     * Method to deal with exceptions that happen in the whole execution (read-process-execute) process of the platform.
     * These exceptions should be handled in the input, normally to report them back to the source of the command (Response
     * in REST request for example).
     * @param outputException Exception happening in the platform's execution process. This value is null if the process went correctly (No exception was raised)
     */
    void response(ModuleExecutionException outputException);
}
