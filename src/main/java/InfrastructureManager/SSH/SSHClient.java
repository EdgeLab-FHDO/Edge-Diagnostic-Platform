package InfrastructureManager.SSH;

import InfrastructureManager.MasterOutput;
import com.jcraft.jsch.JSch;

public class SSHClient extends MasterOutput {

    private JSch jsch;

    public SSHClient(String name) {
        super(name);
        this.jsch = new JSch();
    }

    @Override
    public void out(String response) throws IllegalArgumentException {

    }
}
