package InfrastructureManager;

public class MasterUtility implements MasterOutput {
    @Override
    public void out(String response) {
        switch (response){
            case "exit" :
                Master.getInstance().exitAll();
        }
    }
}
