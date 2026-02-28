package loggingframework.appender;

import loggingframework.core.LogMessage;

public interface Appender {
    void append(LogMessage logMessage);
}
