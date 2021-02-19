package InfrastructureManager.Modules.AdvantEDGE.Exception;

/**
 * Signals an error in interacting with AdvantEdge's API. Normally thrown after a bad response (e.g 404, 500, etc.) is
 * received back from the application.
 */
public class ErrorInResponseException extends AdvantEdgeModuleException {
    /**
     * Constructor of the class.
     * @param message Message to be passed to the exception
     */
    public ErrorInResponseException(String message) {
        super(message);
    }
}
