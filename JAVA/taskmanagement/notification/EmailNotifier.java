package taskmanagement.notification;

import taskmanagement.enums.TaskEventType;
import taskmanagement.model.Task;

public class EmailNotifier implements TaskObserver {
    @Override
    public void onTaskEvent(Task task, TaskEventType eventType) {
        // In real life: send HTTP request to email service
        System.out.println("[EMAIL] Event: " + eventType +
                " | Task: " + task.getTitle() +
                " | Assignee: " + task.getAssigneeId());
    }
}
