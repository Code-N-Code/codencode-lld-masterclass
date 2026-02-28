package loggingframework.demo;

import loggingframework.appender.ConsoleAppender;
import loggingframework.core.LogLevel;
import loggingframework.core.Logger;
import loggingframework.core.LoggerManager;
import loggingframework.formatter.DefaultFormatter;

public class LoggingDemo {
    public static void main(String[] args) {


        Logger logger = LoggerManager.getInstance().getLogger(LoggingDemo.class.getName());

        logger.addAppender(new ConsoleAppender(new DefaultFormatter()));
        logger.setLevel(LogLevel.FATAL);

        logger.fatal("This is an error message");
    }
}
