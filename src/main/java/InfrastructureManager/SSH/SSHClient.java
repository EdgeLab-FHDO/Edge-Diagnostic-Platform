package InfrastructureManager.SSH;

import InfrastructureManager.MasterOutput;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class SSHClient extends MasterOutput {

    private final JSch jsch;
    private String username;
    private String host;
    private String password;
    private int port;


    public SSHClient(String name) {
        super(name);
        this.jsch = new JSch();
        this.username = null;
        this.password = null;
        this.host = null;
        this.port = 0;
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ");
        if (command[0].equals("ssh")) { //The commands must come like "ssh command"
            try {
                switch (command[1]) {
                    case "execute":
                        String toSend = String.join(" ", Arrays.copyOfRange(command,3,command.length));
                        switch (command[2]) {
                            case "-b" :
                                execute(toSend,true);
                                break;
                            case "-f":
                                execute(toSend, false);
                                break;
                            default:
                                throw new IllegalArgumentException("Display parameter missing in SSHClient command");
                        }
                        break;
                    case "setup" :
                        setUpClient(command[2],command[3],command[4], command[5]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for SSHClient");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command  - SSHClient");
            }
        }
    }

    private void execute(String command, boolean background) {
        if (!isSetUp()) {
            throw new IllegalStateException("SSH Client has not been set up");
        }
        Session session = null;
        ChannelExec channel = null;
        command = background ? command + " > /dev/null 2>&1 &" : command;
        try {
            session = jsch.getSession(username,host,port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            ByteArrayOutputStream responseStream = null;
            if (!background) {
                responseStream = new ByteArrayOutputStream();
                channel.setOutputStream(responseStream);
            }
            channel.connect();
            do {
                Thread.sleep(100);
            } while (channel.isConnected());


            if (!background) {
                String responseString = new String(responseStream.toByteArray());
                System.out.println(responseString);
            }

        } catch (JSchException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }
    private void setUpClient(String host, String port, String username, String password) {
        this.username = username;
        this.host = host;
        this.password = password;
        this.port = Integer.parseInt(port);
    }

    private boolean isSetUp() {
        return this.username != null && this.host != null && this.port != 0 && this.password != null;
    }
}
