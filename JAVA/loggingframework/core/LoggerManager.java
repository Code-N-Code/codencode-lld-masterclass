package loggingframework.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoggerManager {
    private static final LoggerManager INSTANCE = new LoggerManager();
    private final Map<String, Logger> loggers;

    private LoggerManager() {
        loggers = new ConcurrentHashMap<>();
    }

    public static LoggerManager getInstance() {
        return INSTANCE;
    }

    public Logger getLogger(String name) {
        return loggers.computeIfAbsent(name,
                key -> new Logger(key, LogLevel.INFO));
    }
}
