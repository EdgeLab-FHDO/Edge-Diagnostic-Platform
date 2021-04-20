package InfrastructureManager.Modules.RemoteExecution.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.ClientNotInitializedException;
import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.CommandExecution.CommandExecutionException;
import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.FileSending.FileSendingException;
import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.FileSending.InvalidFileException;
import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.FileSending.SCPProtocolException;
import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.SSHException;
import InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModuleObject;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.Arrays;

/**
 * Class that represents interaction with remote terminals via SSH as an output of the platform.
 * <p>
 * This type of output is used for interacting with remote UNIX terminals (BASH) via SSH.
 * <p>
 * It provides the following functionalities:
 * <p>
 * - Execute commands in a remote machine (both sudo and normal)
 * - Send files to a remote machine via SCP
 *
 * It uses the external {@link JSch} to allow for SSH connection
 */
public class SSHClient extends RemoteExecutionModuleObject implements PlatformOutput {

    private final JSch jsch;
    private String username;
    private String host;
    private String password;
    private int port;


    /**
     * Constructor of the class. Creates a new SSHClient.
     *
     * @param module Owner module of this output
     * @param name   Name of this output. Normally hardcoded as "MODULE_NAME.ssh.out"
     */
    public SSHClient(ImmutablePlatformModule module, String name) {
        super(module, name);
        this.jsch = new JSch();
        this.username = null;
        this.password = null;
        this.host = null;
        this.port = 0;
    }

    /**
     * Based on processed responses from the inputs executes the different functionalities
     *
     * @param response Must be of the way "ssh COMMAND", and depending on the command receives additional parameters:
     *                 - "execute": A display parameter (which can be "-b" for the remote command to be executed in the background or "-f" for the opposite)
     *                 and a command to execute remotely (can have spaces).
     *                 - "setUp": Hostname (address), SSH port on host, Username and Password in the remote machine.
     *                 - "sendFile": Path to the local file to be sent and Path in the remote machine to send to
     *
     * @throws SSHException If the passed command is invalid or is missing parameters.
     */
    @Override
    public void execute(String response) throws SSHException {
        String[] command = response.split(" ");
        this.getLogger().debug(this.getName() , "SSH execute, response:"+ response);
        if (command[0].equals("ssh")) { //The commands must come like "ssh command"
            this.getLogger().debug(this.getName() , "SSH command additional cmds: "+ command[1]);
            try {
                switch (command[1]) {
                    case "execute" -> {
                        String toSend = String.join(" ", Arrays.copyOfRange(command, 3, command.length));
                        switch (command[2]) {
                            case "-b" -> execute(toSend, true);
                            case "-f" -> execute(toSend, false);
                            default -> throw new SSHException("Wrong Display parameter in SSHClient execute command");
                        }
                    }
                    case "setup" -> setUpClient(command[2], command[3], command[4], command[5]);
                    case "sendFile" -> sendFile(command[2], command[3]);
                    default -> throw new SSHException("Invalid command " + command[1] + " for SSHClient");
                }
            } catch (IndexOutOfBoundsException e){
                throw new SSHException("Arguments missing for command " + response + " to SSHClient");
            }
        }
    }

    /**
     * Sends a file to a remote machine usign SCP protocol
     *
     * @param localFilePath      the local file path
     * @param remoteDestFilePath the remote destination file path
     * @throws FileSendingException          If an error occurs while sending the file
     * @throws ClientNotInitializedException If this function is called before setting up the client with the {@link #setUpClient(String, String, String, String)} method
     */
    private void sendFile(String localFilePath, String remoteDestFilePath) throws FileSendingException, ClientNotInitializedException {
        if (!isSetUp()) {
            throw new ClientNotInitializedException("SSH Client has not been set up");
        }
        this.getLogger().debug(this.getName(),"Sending a file with path: "+ localFilePath + " with dest add:"+ remoteDestFilePath);
        //Resources declaration in null, so they can be closed in the finally
        Session session = null;
        ChannelExec channel = null;
        OutputStream out = null;
        InputStream in = null;
        //-----------------------------
        File localFile = new File(localFilePath);
        if (!localFile.isFile()) {
            throw new InvalidFileException("Invalid File in " + localFilePath);
        }
        //Try-with-resources, closes the stream automatically
        try (FileInputStream fileStream = new FileInputStream(localFile)) {
            this.getLogger().debug(this.getName(),"Creating a session");
            session = createSession();
            this.getLogger().debug(this.getName(),"Connecting to SSH session");
            session.connect(); //Connect to SSH Session

            String command = "scp -t " + remoteDestFilePath; //Execute scp -t file in the remote
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            out = channel.getOutputStream(); //Get stream to send to remote
            in = channel.getInputStream(); //Get stream to receive from remote

            this.getLogger().debug(this.getName(),"Start the channel and send the command");
            channel.connect(); //Start the channel and send the command

            checkSCPAck(in); //Check for SCP acknowledge

            // send "C0644 fileSize filename" according to SCP Protocol, end with new line char
            command="C0644 "+ localFile.length() +" " + localFile.getName() + "\n";
            out.write(command.getBytes());
            out.flush();

            checkSCPAck(in); //Check if received

            this.getLogger().debug(this.getName(),"Sending content of the file");
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

        } catch (JSchException | IOException e) {
            throw new FileSendingException("Error while sending file", e);
        } finally {
            //Close IO Resources
            this.getLogger().debug(this.getName(),"Closing IO Resources");
            if (channel != null) channel.disconnect();
            if (session != null) session.disconnect();
            if (in != null) try {in.close();} catch (IOException ignored) {}
            if (out != null) try {out.close();} catch (IOException ignored) {}
        }
    }

    /**
     * Check the acknowledge response as defined in the SCP protocol.
     *
     * @param in Input stream coming from the remote machine (response to SCP send)
     * @throws IOException          If an error happens while reading the stream
     * @throws SCPProtocolException If the acknowledge response is different from "0". In that case, reads the error message
     * from the stream and adds it to the exception
     */
    private void checkSCPAck(InputStream in) throws IOException, SCPProtocolException {
        this.getLogger().debug(this.getName(),"Checking for ack response ");
        int ack=in.read();
        this.getLogger().debug(this.getName(),"Ack response: "+ack);
        switch (ack) {
            case 0: return;
            case 1:
            case 2:
                throw new SCPProtocolException(getErrorFromInputStream(in)); //If response is 1 or 2 then print the error message
            default:
                throw new SCPProtocolException("Unknown error");
        }
    }

    /**
     * Reads error messages from the input stream (Response from SCP).
     *
     * @param in Input stream coming from the remote machine
     * @return the error  read from the input stream
     * @throws IOException If an error happens while reading from the stream
     */
    private String getErrorFromInputStream (InputStream in) throws IOException {
        this.getLogger().debug(this.getName(),"Response from SCP");
        int ch;
        StringBuilder sb=new StringBuilder();
        do {
            ch=in.read();
            sb.append((char)ch);
        }
        while(ch!='\n'); //Read the error message for responses 1 and 2
        return sb.toString();
    }

    /**
     * Executes a command in a remote machine using SSH. The command can be run in the foreground or background
     * of the remote machine and can also use admin benefits (sudo commands)
     *
     * @param command    Command to execute in the remote machine
     * @param background If true, the command will be executed in the background in the remote machine
     * @throws CommandExecutionException     If an error happens while executing or sending the command
     * @throws ClientNotInitializedException If this function is called before the client is set up using the {@link #setUpClient(String, String, String, String)} method.
     */
    private void execute(String command, boolean background) throws CommandExecutionException, ClientNotInitializedException {
        if (!isSetUp()) {
            throw new ClientNotInitializedException("SSH Client has not been set up");
        }
        this.getLogger().debug(this.getName(), "Executing command on remote machine, cmd: "+command+", background_flag: "+background);
        //Resources declaration in null, so they can be closed in the finally
        Session session = null;
        ChannelExec channel = null;
        ByteArrayOutputStream responseStream = null;
        OutputStream toChannel = null;
        //-------------------------------------------
        boolean sudo = command.startsWith("sudo");
        command = modifyCommand(command,background,sudo); //Modify the command according to if its sudo and background

        try {
            this.getLogger().debug(this.getName(),"Crating and connecting to SSH Session");
            session = createSession();
            session.connect(); //Connect to the SSH session

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command); //Create an execution channel and set the command

            if (!background) { //If commands runs on the front, create a stream to sink its output
                this.getLogger().debug(this.getName(),"Creating a stream to sink its output");
                responseStream = new ByteArrayOutputStream();
                channel.setOutputStream(responseStream);
            }
            if (sudo) toChannel = channel.getOutputStream(); //If command is sudo, create a stream to be able to send password

            channel.connect(); //Connect to the channel and send the command

            if (sudo) { //If sudo command,write the password to the STDIN
                this.getLogger().debug(this.getName(),"Writing the password to the STDIN");
                toChannel.write((password + "\n").getBytes());
                toChannel.flush();
            }
            if (!background) { //If the command runs on the front, print its output to the console
                this.getLogger().debug(this.getName(),"Cmd running on front, print to console");
                do {
                    Thread.sleep(100);
                } while (channel.isConnected()); //Wait for command to be over in the remote
                String responseString = new String(responseStream.toByteArray());
                System.out.println(responseString);
            }

        } catch (JSchException | InterruptedException | IOException e) {
            throw new CommandExecutionException("Error while executing command", e);
        } finally {
            //Close IO Resources
            this.getLogger().debug(this.getName(),"Closing IO Resources");
            if (channel != null) channel.disconnect();
            if (session != null) session.disconnect();
            if (responseStream != null) try {responseStream.close();} catch (IOException ignored) {}
            if (toChannel != null) try {toChannel.close();} catch (IOException ignored) {}
        }
    }

    /**
     * Create SSH session.
     *
     * @return Created {@link Session} to interact with remote machine
     * @throws JSchException If an error happens while creating the session
     */
    private Session createSession() throws JSchException {
        this.getLogger().debug(this.getName(),"Creating SSH session");
        Session session = jsch.getSession(username,host,port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        return session;
    }

    /**
     * Modifies the command based on whether is a sudo command or not, and whether it should be executed in background or not.
     *
     * @param command    Unprocessed command to be sent to remote
     * @param background True if the command is to be executed in the background
     * @param sudo       True if it is a sudo command
     * @return Command ready to be sent to the remote
     */
    private String modifyCommand(String command, boolean background, boolean sudo) {
        this.getLogger().debug(this.getName(),"Modifying the command:"+command+", back ground flag is "+background+" & sudo flag is "+sudo);
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

    /**
     * Sets up the SSH client by adding connection parameters. This method should be called before doing anything else with the client
     *
     * @param host     Name (Address) of the host
     * @param port     SSH port of the host
     * @param username Username in the host
     * @param password Password for the username
     */
    private void setUpClient(String host, String port, String username, String password) {
        this.getLogger().debug(this.getName(),"Setting up client, host: "+host+",port: "+port+",username: "+username+",pass: "+password);
        this.username = username;
        this.host = host;
        this.password = password;
        this.port = Integer.parseInt(port);
    }

    /**
     * Checks if the client has been set up
     *
     * @return true is if has, false otherwise
     */
    private boolean isSetUp() {
        return this.username != null && this.host != null && this.port != 0 && this.password != null;
    }
}
