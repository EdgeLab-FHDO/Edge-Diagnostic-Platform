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
        String[] command = response.split(" ",3);
        if (command[0].equals("ssh")) { //The commands must come like "ssh command"
            try {
                switch (command[1]) {
                    case "execute":
                        execute(command[2]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for SSHClient");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command  - SSHClient");
            }
        }
    }

    private void execute(String command) {
        System.out.println("To execute remotely: " + command);
    }
}
