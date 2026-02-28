package loggingframework.formatter;

import loggingframework.core.LogMessage;

import java.time.format.DateTimeFormatter;

public class DefaultFormatter implements Formatter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    @Override
    public String format(LogMessage logMessage) {
        return String.format(
                "%s [%s] %s %s - %s",
                logMessage.getTimestamp().format(formatter),
                logMessage.getThreadName(),
                logMessage.getLoggerName(),
                logMessage.getLevel(),
                logMessage.getMessage()
        );
    }
}
