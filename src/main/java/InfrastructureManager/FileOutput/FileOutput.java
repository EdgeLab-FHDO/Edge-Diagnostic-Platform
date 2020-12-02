package InfrastructureManager.FileOutput;

import InfrastructureManager.MasterOutput;

import java.io.*;

public class FileOutput extends MasterOutput {

    public FileOutput(String name) {
        super(name);
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ");
        if (command[0].equals("file_out")) { //The commands must come like "file_out command"
            try {
                switch (command[1]) {
                    case "create":
                        writeToFile(command[2], command[3]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for FileOutput");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command - FileOutput");
            }
        }
    }

    private void writeToFile(String path, String content) {
        File file = new File(path);
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] toWrite = content.getBytes();
            out.write(toWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
