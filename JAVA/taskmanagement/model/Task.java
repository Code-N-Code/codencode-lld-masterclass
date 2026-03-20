package taskmanagement.model;

import taskmanagement.enums.Priority;
import taskmanagement.enums.TaskStatus;

import java.time.LocalDate;
import java.util.UUID;

public class Task {
    private final String taskId;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private String assigneeId;
    private LocalDate dueDate;

    public Task(String title, String description, Priority priority, LocalDate dueDate) {
        this.taskId = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public void setStatus(TaskStatus status) {
        // State machine guard — discuss this in interview!
        if (this.status == TaskStatus.COMPLETED) {
            throw new IllegalStateException("Cannot change status of a completed task.");
        }
        this.status = status;
    }

    // Standard getters
    public String getTaskId()       { return taskId; }
    public String getTitle()        { return title; }
    public String getDescription()  { return description; }
    public TaskStatus getStatus()   { return status; }
    public Priority getPriority()   { return priority; }
    public String getAssigneeId()   { return assigneeId; }
    public LocalDate getDueDate()   { return dueDate; }

    public void setAssigneeId(String assigneeId) { this.assigneeId = assigneeId; }
}
