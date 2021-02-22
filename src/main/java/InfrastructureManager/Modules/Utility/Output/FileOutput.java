package InfrastructureManager.Modules.Utility.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.Utility.Exception.FileOutput.FileOutputException;
import InfrastructureManager.Modules.Utility.Exception.FileOutput.InvalidEncodingException;
import InfrastructureManager.Modules.Utility.UtilityModuleObject;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;

/**
 * Class allowing to provide file creation as an output of the platform.
 * <p>
 * This type of output is used for creating and writing in a file.
 * <p>
 * It provides the following functionalities:
 * <p>
 * - Creating a file with a desired encoding and content.
 * - Appending content to an existing file
 */
public class FileOutput extends UtilityModuleObject implements PlatformOutput {

    private Charset encoding;

    /**
     * Creates a new FileOutput.
     *
     * @param module Owner module of this output
     * @param name   Name of this output. Normally hardcoded "MODULE_NAME.fileOut"
     */
    public FileOutput(ImmutablePlatformModule module, String name) {
        super(module,name);
        this.encoding = StandardCharsets.UTF_8;
    }

    /**
     * Based on processed responses from the inputs executes the different functionalities
     * @param response Must be in a way: "file_out COMMAND" and additionally:
     *                 - Encoding type following the "encoding" command
     *                 - Path and content following the "create" and "append" commands
     * @throws FileOutputException If the passed command is invalid or is missing arguments
     */
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

    /**
     * Gets the current encoding used for output files.
     *
     * @return the encoding as a {@link Charset} element
     */
    public Charset getEncoding() {
        return encoding;
    }

    /**
     * Sets the encoding of the output files.
     *
     * @param encodingName the encoding name.
     * @see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html">Java Supported Encodings</a>
     * @throws InvalidEncodingException If an invalid or not supported encoding is used
     */
    public void setEncoding(String encodingName) throws InvalidEncodingException {
        try {
            this.encoding = Charset.forName(encodingName);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new InvalidEncodingException("Specified encoding " + encodingName + " is not valid", e);
        }
    }

    /**
     * Writes a string into a file
     * @param path Path for the result file
     * @param content String with content to put on the file. Can contain spaces and special characters (according to selected encoding)
     * @param append If the file in path already exists and this is true, the file will be appended instead of overwritten.
     *               If no file exists, a new one will be created regardless of this parameter
     * @throws FileOutputException If an error occurs while writing to the file.
     */
    private void writeToFile(String path, String content, boolean append) throws FileOutputException {
        File file = new File(path);
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file,append))) {
            byte[] toWrite = content.getBytes(this.encoding);
            out.write(toWrite);
        } catch (IOException e) {
            throw new FileOutputException("Error while writing to the file in " + path, e);
        }
    }

    /**
     * Allows to extract the rest of the incoming command to the output, to generate a content string for the file that includes spaces
     * @param command Command coming from processed input
     * @return String containing all arguments after the action command ("create" or "append") as a single string.
     */
    private String restOfCommand(String[] command) {
        return String.join(" ",Arrays.copyOfRange(command, 3,command.length));
    }
}
