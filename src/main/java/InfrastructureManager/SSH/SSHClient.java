package InfrastructureManager.SSH;

import InfrastructureManager.MasterOutput;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
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
                                throw new IllegalArgumentException("Wrong Display parameter in SSHClient execute command");
                        }
                        break;
                    case "setup" :
                        setUpClient(command[2],command[3],command[4], command[5]);
                        break;
                    case "sendFile" :
                        sendFile(command [2], command[3]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for SSHClient");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command - SSHClient");
            }
        }
    }

    private void sendFile(String localFilePath, String remoteDestFilePath) {
        if (!isSetUp()) {
            throw new IllegalStateException("SSH Client has not been set up");
        }
        //Resources declaration in null, so they can be closed in the finally
        Session session = null;
        ChannelExec channel = null;
        OutputStream out = null;
        InputStream in = null;
        //-----------------------------
        File localFile = new File(localFilePath);
        if (!localFile.isFile()) {
            throw new IllegalArgumentException("Invalid File");
        }
        //Try-with-resources, closes the stream automatically
        try (FileInputStream fileStream = new FileInputStream(localFile)) {

            session = createSession();
            session.connect(); //Connect to SSH Session

            String command = "scp -t " + remoteDestFilePath; //Execute scp -t file in the remote
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            out = channel.getOutputStream(); //Get stream to send to remote
            in = channel.getInputStream(); //Get stream to receive from remote

            channel.connect(); //Start the channel and send the command

            checkSCPAck(in); //Check for SCP acknowledge

            // send "C0644 fileSize filename" according to SCP Protocol, end with new line char
            command="C0644 "+ localFile.length() +" " + localFile.getName() + "\n";
            out.write(command.getBytes());
            out.flush();

            checkSCPAck(in); //Check if received

            // send content of the file
            byte[] buf=new byte[1024];
            while(true){
                int len= fileStream.read(buf, 0, buf.length);
                if(len<=0) break;
                out.write(buf, 0, len);
            }

            // send '\0' to finish
            buf[0]=0; out.write(buf, 0, 1);
            out.flush();

            checkSCPAck(in); //check received


        } catch (JSchException | IOException | SCPException e) {
            e.printStackTrace();
        } finally {
            //Close IO Resources
            if (channel != null) channel.disconnect();
            if (session != null) session.disconnect();
            if (in != null) try {in.close();} catch (IOException ignored) {}
            if (out != null) try {out.close();} catch (IOException ignored) {}
        }
    }

    private void checkSCPAck(InputStream in) throws IOException, SCPException {
        int ack=in.read();
        switch (ack) {
            case 0: return;
            case 1:
            case 2:
                throw new SCPException(getErrorFromInputStream(in)); //If response is 1 or 2 then print the error message
            default:
                throw new SCPException("Unknown error");
        }
    }

    private String getErrorFromInputStream (InputStream in) throws IOException {
        int ch;
        StringBuilder sb=new StringBuilder();
        do {
            ch=in.read();
            sb.append((char)ch);
        }
        while(ch!='\n'); //Read the error message for responses 1 and 2
        return sb.toString();
    }

    private void execute(String command, boolean background) {
        if (!isSetUp()) {
            throw new IllegalStateException("SSH Client has not been set up");
        }
        //Resources declaration in null, so they can be closed in the finally
        Session session = null;
        ChannelExec channel = null;
        ByteArrayOutputStream responseStream = null;
        OutputStream toChannel = null;
        //-------------------------------------------
        boolean sudo = command.startsWith("sudo");
        command = modifyCommand(command,background,sudo); //Modify the command according to if its sudo and background

        try {
            session = createSession();
            session.connect(); //Connect to the SSH session

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command); //Create an execution channel and set the command

            if (!background) { //If commands runs on the front, create a stream to sink its output
                responseStream = new ByteArrayOutputStream();
                channel.setOutputStream(responseStream);
            }
            if (sudo) toChannel = channel.getOutputStream(); //If command is sudo, create a stream to be able to send password

            channel.connect(); //Connect to the channel and send the command

            if (sudo) { //If sudo command,write the password to the STDIN
                toChannel.write((password + "\n").getBytes());
                toChannel.flush();
            }
            if (!background) { //If the command runs on the front, print its output to the console
                do {
                    Thread.sleep(100);
                } while (channel.isConnected()); //Wait for command to be over in the remote
                String responseString = new String(responseStream.toByteArray());
                System.out.println(responseString);
            }

        } catch (JSchException | InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            //Close IO Resources
            if (channel != null) channel.disconnect();
            if (session != null) session.disconnect();
            if (responseStream != null) try {responseStream.close();} catch (IOException ignored) {}
            if (toChannel != null) try {toChannel.close();} catch (IOException ignored) {}
        }
    }

    private Session createSession() throws JSchException {
        Session session = jsch.getSession(username,host,port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        return session;
    }

    private String modifyCommand(String command, boolean background, boolean sudo) {
        if (background && sudo) {
            return command.replaceFirst("sudo\\s+","sudo -b -S ") + " > /dev/null 2>&1";
        } else if (background) {
            return command + " > /dev/null 2>&1 &";
        } else if (sudo) {
            return command.replaceFirst("sudo\\s+","sudo -S ");
        } else {
            return command;
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
