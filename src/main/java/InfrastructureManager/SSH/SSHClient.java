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
        Session session = null;
        ChannelExec channel = null;
        try {
            session = jsch.getSession(username,host,port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }

            String responseString = new String(responseStream.toByteArray());
            System.out.println(responseString);
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
