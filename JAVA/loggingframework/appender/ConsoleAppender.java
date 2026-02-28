package loggingframework.appender;

import loggingframework.core.LogMessage;
import loggingframework.formatter.Formatter;

public class ConsoleAppender implements Appender{
    private final Formatter formatter;

    public ConsoleAppender(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void append(LogMessage logMessage) {
        System.out.println(formatter.format(logMessage));
    }
}
