package InfrastructureManager.Modules.Utility;

import InfrastructureManager.ModuleManagement.ModuleOutput;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.Utility.Exception.FileOutput.FileOutputException;
import InfrastructureManager.Modules.Utility.Exception.FileOutput.InvalidEncodingException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;

public class FileOutput extends ModuleOutput {

    private Charset encoding;

    public FileOutput(PlatformModule module, String name) {
        super(module,name);
        this.encoding = StandardCharsets.UTF_8;
    }

    @Override
    public void execute(String response) throws FileOutputException {
        String[] command = response.split(" ");
        if (command[0].equals("file_out")) { //The commands must come like "file_out command"
            try {
                switch (command[1]) {
                    case "encoding" :
                        setEncoding(command[2]);
                    case "create":
                        writeToFile(command[2], restOfCommand(command), false);
                        break;
                    case "append":
                        writeToFile(command[2], restOfCommand(command), true);
                        break;
                    default:
                        throw new FileOutputException("Invalid command " + command[1] + " for FileOutput");
                }
            } catch (IndexOutOfBoundsException e){
                throw new FileOutputException("Arguments missing for command " + response + " to FileOutput");
            }
        }
    }

    public Charset getEncoding() {
        return encoding;
    }

    public void setEncoding(String encodingName) throws InvalidEncodingException {
        try {
            this.encoding = Charset.forName(encodingName);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new InvalidEncodingException("Specified encoding " + encodingName + " is not valid", e);
        }
    }

    private void writeToFile(String path, String content, boolean append) throws FileOutputException {
        File file = new File(path);
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file,append))) {
            byte[] toWrite = content.getBytes(this.encoding);
            out.write(toWrite);
        } catch (IOException e) {
            throw new FileOutputException("Error while writing to the file in " + path, e);
        }
    }

    private String restOfCommand(String[] command) {
        return String.join(" ",Arrays.copyOfRange(command, 3,command.length));
    }
}
