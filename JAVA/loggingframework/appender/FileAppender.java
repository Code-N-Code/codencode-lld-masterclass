package loggingframework.appender;

import loggingframework.core.LogMessage;
import loggingframework.formatter.Formatter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileAppender implements Appender{

    private final Formatter formatter;
    private final BufferedWriter writer;
    private final Object lock = new Object();

    public FileAppender(String filePath, Formatter formatter) {
        this.formatter = formatter;

        try {
            this.writer = Files.newBufferedWriter(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize FileAppender", e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutDown));
    }

    @Override
    public void append(LogMessage logMessage) {
        synchronized (lock) {
            try {
                writer.write(formatter.format(logMessage));
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void shutDown() {
        synchronized (lock) {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException("Failed to close FileAppender", e);
            }
        }
    }
}
