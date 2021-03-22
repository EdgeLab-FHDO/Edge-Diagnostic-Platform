package DiagnosticsClient;

import DiagnosticsClient.Control.RawData.ClientInstruction;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class InstructionQueue {

    private final BlockingQueue<ClientInstruction> queue;

    public InstructionQueue() {
        this.queue = new ArrayBlockingQueue<>(10);
    }

    public void add(ClientInstruction instruction) throws InterruptedException {
        this.queue.put(instruction);
    }

    public ClientInstruction get() throws InterruptedException {
        return this.queue.take();
    }
}
