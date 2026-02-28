package loggingframework.formatter;

import loggingframework.core.LogMessage;

public interface Formatter {
    String format(LogMessage logMessage);
}
