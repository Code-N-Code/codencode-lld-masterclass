package taskmanagement.notification;

import taskmanagement.enums.TaskEventType;
import taskmanagement.model.Task;

public interface TaskObserver {
    void onTaskEvent(Task task, TaskEventType eventType);
}
