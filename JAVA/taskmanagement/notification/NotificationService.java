package taskmanagement.notification;

import taskmanagement.enums.TaskEventType;
import taskmanagement.model.Task;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationService {
    private final List<TaskObserver> observers = new CopyOnWriteArrayList<>();

    // ExecutorService for async notification dispatch
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TaskObserver observer) {
        observers.remove(observer);
    }

    // Fires async — calling thread is not blocked
    public void notifyObservers(Task task, TaskEventType eventType) {
        for(TaskObserver observer : observers) {
            executor.submit(() -> {
               try {
                   observer.onTaskEvent(task, eventType);
               } catch (Exception e) {
                   System.err.println("Notification failed: " + e.getMessage());
               }
            });
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
