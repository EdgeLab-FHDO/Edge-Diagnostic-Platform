package InfrastructureManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandSet implements MasterConfig<Map<String,String>> {
    private final Map<String,String> commands;
    private final String FILE_PATH = "src/main/resources/commands.txt";

    public CommandSet() {
        this.commands = new HashMap<>();
        readCommandsFromFile();
    }

    @Override
    public void set(Map<String, String> config) {
        //TODO: implement in future
    }

    @Override
    public Map<String, String> get() {

        return this.commands;
    }

    private void readCommandsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.FILE_PATH))) {
            String[] command;
            String line;
            while ((line = reader.readLine()) != null) {
                command = line.split("->");
                this.commands.putIfAbsent(command[0],command[1]);
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public String getCommand(String command) {
        return this.commands.get(command);
    }


}
