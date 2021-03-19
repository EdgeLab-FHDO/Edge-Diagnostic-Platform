package DiagnosticsClient;

import DiagnosticsClient.Control.RawData.Instruction;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class InstructionQueue {

    private final BlockingQueue<Instruction> queue;

    public InstructionQueue() {
        this.queue = new ArrayBlockingQueue<>(10);
    }

    public void add(Instruction instruction) throws InterruptedException {
        this.queue.put(instruction);
    }

    public Instruction get() throws InterruptedException {
        return this.queue.take();
    }
}
