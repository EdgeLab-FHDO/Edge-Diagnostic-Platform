package InfrastructureManager.SSH;

import InfrastructureManager.MasterOutput;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;

public class SSHClient extends MasterOutput {

    private final JSch jsch;
    private final String username = "jpcr3108";
    private final String host = "192.168.0.127";
    private final String password = "jp1234";
    private final int port = 22;


    public SSHClient(String name) {
        super(name);
        this.jsch = new JSch();
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ",4);
        if (command[0].equals("ssh")) { //The commands must come like "ssh command"
            try {
                switch (command[1]) {
                    case "execute":
                        switch (command[2]) {
                            case "-b" :
                                execute(command[3],true);
                                break;
                            case "-f":
                                execute(command[3], false);
                                break;
                            default:
                                throw new IllegalArgumentException("Display parameter missing in SSHClient command");
                        }

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
}
