package loggingframework.core;

import loggingframework.appender.Appender;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Logger {
    private final String loggerName;
    private volatile LogLevel level;
    private final List<Appender> appenders;

    public Logger(String loggerName, LogLevel level) {
        this.loggerName = loggerName;
        this.level = level;
        this.appenders = new CopyOnWriteArrayList<>();
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public void addAppender(Appender appender) {
        this.appenders.add(appender);
    }

    public void log(LogLevel level, String message) {
        if(level.getLevel() < this.level.getLevel()) {
            return;
        }

        LogMessage log = new LogMessage(message, level, loggerName);

        for(Appender appender : this.appenders) {
            appender.append(log);
        }
    }


    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }
    public void info(String message) {
        log(LogLevel.INFO, message);
    }
    public void warn(String message) {
        log(LogLevel.WARNING, message);
    }
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }
    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }
}
