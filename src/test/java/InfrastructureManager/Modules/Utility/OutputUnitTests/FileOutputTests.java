package InfrastructureManager.Modules.Utility.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.Utility.Exception.FileOutput.FileOutputException;
import InfrastructureManager.Modules.Utility.Exception.FileOutput.InvalidEncodingException;
import InfrastructureManager.Modules.Utility.FileOutput;
import InfrastructureManager.Modules.Utility.UtilityModule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static InfrastructureManager.Modules.CommonTestingMethods.assertException;

public class FileOutputTests {

    private final UtilityModule module = new UtilityModule();
    private final FileOutput fileOutput = new FileOutput(module,"util.fileOut");
    private final String testPath = "src/test/resources/Modules/Utility/FileOutput/";

    @BeforeClass
    public static void createTestFolder() throws Exception {
        File testFolder = new File("src/test/resources/Modules/Utility/FileOutput/");
        if (!testFolder.exists()) {
            if (!testFolder.mkdir()) {
                throw new Exception("Test folder could not be created in FileOutputTests");
            }
        }
    }


    @Test
    public void fileIsCreatedTest() throws ModuleExecutionException {
        File file = new File(testPath + "test1");
        fileOutput.execute("file_out create "  + file.getPath() + " Test1");
        Assert.assertTrue(file.exists());
        file.deleteOnExit();
    }

    @Test
    public void fileContentIsCorrectlyWrittenTest() throws IOException, ModuleExecutionException {
        String expected = "Test2Content";
        File file = new File( testPath + "test2");
        fileOutput.execute("file_out create "  + file.getPath() + " Test2Content");
        assertFileContent(file,expected);
    }

    @Test
    public void fileContentCanHaveSpacesTest() throws IOException, ModuleExecutionException {
        String expected = "Test 3\n\tContent";
        File file = new File( testPath + "test3");
        fileOutput.execute("file_out create "  + file.getPath() + " Test 3\n\tContent");
        assertFileContent(file,expected);
    }

    @Test
    public void appendCommandDoesNotOverwriteTest() throws IOException, ModuleExecutionException {
        File file = new File(testPath + "test");
        createFileWithContent(file,"Old Text");
        String expected = "Old Text New Text";
        fileOutput.execute("file_out append " + file.getPath() + "  New Text");
        assertFileContent(file,expected);
    }

    @Test
    public void appendCommandCreatesNewFileIfNecessary() throws IOException, ModuleExecutionException {
        File file = new File(testPath + "file");
        String expected = "test";
        fileOutput.execute("file_out append " + file.getPath() + " test");
        assertFileContent(file,expected);
    }

    @Test
    public void defaultEncodingIsUTF_8Test() {
        String expected = "UTF-8";
        Assert.assertEquals(expected, fileOutput.getEncoding().name());
    }

    @Test
    public void encodingCanBeChangedTest() throws ModuleExecutionException {
        String expected = "UTF-16";
        fileOutput.execute("file_out encoding UTF16");
        Assert.assertEquals(expected, fileOutput.getEncoding().name());
    }

    @Test
    public void fileIsCreatedWithDifferentEncodingTest() throws IOException, ModuleExecutionException {
        String expected = Character.toString(0x2F81A); //Testing with '冬' character, which is differently encoded in UTF8 and UTF16
        File file = new File(testPath + "testEncoding");
        fileOutput.execute("file_out encoding UTF16");
        fileOutput.execute("file_out create " + file.getPath() + " " + expected);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file),StandardCharsets.UTF_16))) {
            Assert.assertEquals(expected,in.readLine());
        } finally {
            file.deleteOnExit();
        }
    }

    @Test
    public void settingInvalidEncodingThrowsException() {
        String command = "file_out encoding XYZ123";
        String expected = "Specified encoding XYZ123 is not valid";
        assertExceptionInOutput(InvalidEncodingException.class, expected, command);
    }

    @Test
    public void invalidCommandThrowsException() {
        String command = "file_out notACommand";
        String expected = "Invalid command notACommand for FileOutput";
        assertExceptionInOutput(FileOutputException.class, expected, command);
    }

    @Test
    public void incompleteCommandThrowsException() {
        String command = "file_out encoding";
        String expected = "Arguments missing for command " + command + " to FileOutput";
        assertExceptionInOutput(FileOutputException.class, expected, command);
    }

    private void assertExceptionInOutput(Class<? extends Exception> exceptionClass, String expectedMessage, String command) {
        assertException(exceptionClass, expectedMessage, () -> fileOutput.execute(command));
    }

    private void assertFileContent(File file, String expected) throws IOException {
        char[] buffer = new char[expected.length()];
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            in.read(buffer);
            Assert.assertEquals(expected, new String(buffer));
        } finally {
            file.deleteOnExit();
        }
    }

    private void createFileWithContent(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }
}
