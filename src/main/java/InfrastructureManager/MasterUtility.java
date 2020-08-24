package InfrastructureManager;

/**
 * Class implementing MasterOutput, for utilities within the master
 */
public class MasterUtility implements MasterOutput {
    /**
     * Out method implementation according to MasterOutput interface, which gets commands from the master
     * @param response Response coming from the master
     */
    @Override
    public void out(String response) {
        switch (response){
            case "exit" :
                Master.getInstance().exitAll();
        }
    }
}
