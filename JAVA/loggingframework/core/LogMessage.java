package loggingframework.core;

import java.time.LocalDateTime;

public class LogMessage {
    private final String message;
    private final LogLevel level;
    private final LocalDateTime timestamp;
    private final String loggerName;
    private final String threadName;


    public LogMessage(String message, LogLevel level, String loggerName) {
        this.message = message;
        this.level = level;
        this.timestamp = LocalDateTime.now();
        this.loggerName = loggerName;
        this.threadName = Thread.currentThread().getName();
    }

    public String getMessage() {
        return message;
    }
    public LogLevel getLevel() {
        return level;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getLoggerName() {
        return loggerName;
    }
    public String getThreadName() {
        return threadName;
    }
}
