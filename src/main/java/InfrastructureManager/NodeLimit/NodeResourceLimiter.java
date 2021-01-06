package InfrastructureManager.NodeLimit;

import InfrastructureManager.MasterInputInterface;
import InfrastructureManager.MasterOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class NodeResourceLimiter  extends MasterOutput implements MasterInputInterface {

    private Map<String,String> limitNodes;

    private final ObjectMapper mapper;

    private Semaphore readingLock;

    public NodeResourceLimiter(String name) {
        super(name);
        this.mapper = new ObjectMapper();
        this.limitNodes = new LinkedHashMap<>();
        this.readingLock = new Semaphore(0); // Binary semaphore, starts without permits so it will block until a request is made
    }

    @Override
    public String read() throws InterruptedException, JsonProcessingException {
        String toSend = "set_limits " + getReading();
        return toSend;
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ");
        if (command[0].equals("limit")) {
            try {
                switch (command[1]) {
                    case "cores":
                        String period = command.length > 4 ? command[4] : "100000";
                        addToLimitList(command[2], command[3],period);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for NodeLimiter");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - NodeLimiter");
            }
        }
    }

    private void addToLimitList(String tag, String limit, String period_ms) {
        int quota_ms = (int) (Double.parseDouble(limit) * Integer.parseInt(period_ms));
        this.limitNodes.put(tag, quota_ms + "_" + period_ms);
        readingLock.release();
    }

    private String getReading() throws JsonProcessingException, InterruptedException {
        readingLock.acquire();
        return this.mapper.writeValueAsString(this.limitNodes);
    }

    /**
     * Reset the output, used for testing purposes
     */
    public void resetOutput() {
        this.limitNodes = new LinkedHashMap<>();
    }
}
