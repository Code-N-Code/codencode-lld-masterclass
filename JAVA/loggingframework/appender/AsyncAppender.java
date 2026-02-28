package loggingframework.appender;

import loggingframework.core.LogMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncAppender implements Appender {

    private final BlockingQueue<LogMessage> queue;
    private final Appender delegate;
    private final Thread workerThread;
    private volatile boolean running = true;

    private static final int BATCH_SIZE = 50;

    public AsyncAppender(Appender delegate, int capacity) {
        this.delegate = delegate;
        this.queue = new LinkedBlockingQueue<>(capacity);

        this.workerThread = new Thread(this::processLogs, "AsyncLogger-Worker");
        this.workerThread.setDaemon(true);
        this.workerThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    @Override
    public void append(LogMessage message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processLogs() {
        while (running || !queue.isEmpty()) {
            try {
                List<LogMessage> batch = new ArrayList<>();
                queue.drainTo(batch, BATCH_SIZE);

                if (batch.isEmpty()) {
                    batch.add(queue.take());
                }

                for (LogMessage message : batch) {
                    delegate.append(message);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void shutdown() {
        running = false;
        workerThread.interrupt();
    }
}