package InfrastructureManager.NodeLimit;

import InfrastructureManager.MasterInput;
import InfrastructureManager.MasterOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class NodeResourceLimiter  extends MasterOutput implements MasterInput {

    private final Map<String,String> limitNodes;

    private final ObjectMapper mapper;

    /*
    Guarded block and boolean to make the input block until there is content to send to the master
     */
    private final Object blockLock;
    private volatile boolean block;

    public NodeResourceLimiter(String name) {
        super(name);
        this.mapper = new ObjectMapper();
        this.limitNodes = new LinkedHashMap<>();
        this.blockLock = new Object();
        this.block = true;
    }

    @Override
    public String read() throws Exception {
        String toSend = "set_limits " + getReading();
        block = true;
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
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - RESTOutput");
            }
        }
    }

    private void addToLimitList(String tag, String limit, String period_ms) {
        int quota_ms = (int) (Double.parseDouble(limit) * Integer.parseInt(period_ms));
        this.limitNodes.put(tag, quota_ms + "_" + period_ms);
        unBlock();
    }

    private String getReading() throws JsonProcessingException {
        while (block) {
            synchronized (blockLock) {
                try {
                    blockLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.mapper.writeValueAsString(this.limitNodes);
    }

    /**
     * Unblock the reading, so a value can be returned to the master
     */
    private void unBlock() {
        synchronized (blockLock) {
            block = false;
            blockLock.notifyAll();
        }
    }
}
