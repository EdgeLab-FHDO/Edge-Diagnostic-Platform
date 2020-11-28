package InfrastructureManager.SSH;

import InfrastructureManager.MasterOutput;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.Arrays;
import java.util.IllegalFormatCodePointException;

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
                    case "sendFile" :
                        sendFile(command [2], command[3]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for SSHClient");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command  - SSHClient");
            }
        }
    }

    private void sendFile(String localFilePath, String remoteDestFilePath) {
        if (!isSetUp()) {
            throw new IllegalStateException("SSH Client has not been set up");
        }
        Session session = null;
        ChannelExec channel = null;
        File localFile = new File(localFilePath);
        FileInputStream fileStream = null;
        if (!localFile.isFile()) {
            throw new IllegalArgumentException("Invalid File");
        }
        try {
            session = createSession();
            session.connect();

            String command = "scp -t " + remoteDestFilePath;
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            checkSCPAck(in);

            // send "C0644 filesize filename", where filename should not include '/'
            command="C0644 "+ localFile.length() +" " + localFile.getName() + "\n";
            out.write(command.getBytes());
            out.flush();

            checkSCPAck(in);

            // send a content of lfile
            fileStream = new FileInputStream(localFile);
            byte[] buf=new byte[1024];
            while(true){
                int len= fileStream.read(buf, 0, buf.length);
                if(len<=0) break;
                out.write(buf, 0, len);
            }
            fileStream.close();

            // send '\0'
            buf[0]=0; out.write(buf, 0, 1);
            out.flush();
            checkSCPAck(in);

            out.close();

        } catch (JSchException | IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    private void checkSCPAck(InputStream in) throws IOException{
        int ack=in.read();
        switch (ack) {
            case 0: return;
            case 1:
            case 2:
                System.out.println(getErrorFromInputStream(in));
                return;
            default:
                System.out.println("Error");
        }
    }

    private String getErrorFromInputStream (InputStream in) throws IOException {
        int ch;
        StringBuilder sb=new StringBuilder();
        do {
            ch=in.read();
            sb.append((char)ch);
        }
        while(ch!='\n');
        return sb.toString();
    }

    private void execute(String command, boolean background) {
        if (!isSetUp()) {
            throw new IllegalStateException("SSH Client has not been set up");
        }
        boolean sudo = command.startsWith("sudo");
        Session session = null;
        ChannelExec channel = null;
        ByteArrayOutputStream responseStream = null;
        command = modifyCommand(command,background,sudo);

        try {
            session = createSession();
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            if (!background) {
                responseStream = new ByteArrayOutputStream();
                channel.setOutputStream(responseStream);
            }
            OutputStream toChannel = channel.getOutputStream();
            channel.connect();

            if (sudo) { //If sudo command,write the password to the STDIN
                toChannel.write((password + "\n").getBytes());
                toChannel.flush();
            }

            do {
                Thread.sleep(100);
            } while (channel.isConnected());


            toChannel.close();

            if (!background) {
                String responseString = new String(responseStream.toByteArray());
                System.out.println(responseString);
            }

        } catch (JSchException | InterruptedException | IOException e) {
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
