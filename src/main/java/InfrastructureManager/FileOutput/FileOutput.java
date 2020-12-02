package InfrastructureManager.FileOutput;

import InfrastructureManager.MasterOutput;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileOutput extends MasterOutput {

    private Charset encoding;

    public FileOutput(String name) {
        super(name);
        this.encoding = StandardCharsets.UTF_8;
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ");
        if (command[0].equals("file_out")) { //The commands must come like "file_out command"
            try {
                switch (command[1]) {
                    case "encoding" :
                        setEncoding(command[2]);
                    case "create":
                        writeToFile(command[2], restOfCommand(3, command));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for FileOutput");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command - FileOutput");
            }
        }
    }

    public void setEncoding(String encodingName) {
        this.encoding = Charset.forName(encodingName);
    }

    private void writeToFile(String path, String content) {
        File file = new File(path);
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] toWrite = content.getBytes(this.encoding);
            out.write(toWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String restOfCommand(int position, String[] command) {
        return String.join(" ",Arrays.copyOfRange(command,position,command.length));
    }
}
