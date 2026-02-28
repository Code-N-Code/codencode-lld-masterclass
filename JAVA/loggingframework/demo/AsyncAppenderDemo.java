package loggingframework.demo;

import loggingframework.appender.AsyncAppender;
import loggingframework.appender.ConsoleAppender;
import loggingframework.appender.FileAppender;
import loggingframework.core.LogLevel;
import loggingframework.core.Logger;
import loggingframework.core.LoggerManager;
import loggingframework.formatter.DefaultFormatter;
import loggingframework.formatter.Formatter;

public class AsyncAppenderDemo {

    public static void main(String[] args) throws InterruptedException {

        // Get logger
        Logger logger = LoggerManager.getInstance()
                .getLogger(AsyncAppenderDemo.class.getName());

        logger.setLevel(LogLevel.DEBUG);

        // Create formatter
        Formatter formatter = new DefaultFormatter();

        // Create appender
        FileAppender fileAppender = new FileAppender("async-demo.log", formatter);
        ConsoleAppender consoleAppender = new ConsoleAppender(formatter);

        // Wrap file appender inside AsyncAppender
        AsyncAppender asyncAppender = new AsyncAppender(fileAppender, 10000);

        // Add appender to logger
        logger.addAppender(consoleAppender);     // Sync console
        logger.addAppender(asyncAppender);      // Async file

        System.out.println("Starting async logging demo...");

        // Create multiple threads to simulate real load
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            for (int i = 1; i <= 50; i++) {
                logger.debug("Message " + i + " from " + threadName);
            }
        };

        Thread t1 = new Thread(task, "Worker-1");
        Thread t2 = new Thread(task, "Worker-2");
        Thread t3 = new Thread(task, "Worker-3");

        long start = System.currentTimeMillis();

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        long end = System.currentTimeMillis();

        System.out.println("Logging calls completed in " + (end - start) + " ms");

        // Give async worker time to finish remaining logs
        Thread.sleep(2000);

        System.out.println("Async logging demo finished.");
    }
}