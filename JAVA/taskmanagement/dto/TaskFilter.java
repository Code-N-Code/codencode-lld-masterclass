package taskmanagement.dto;

import taskmanagement.enums.Priority;
import taskmanagement.enums.TaskStatus;

// Used to filter task listings — great Builder pattern candidate
public class TaskFilter {
    private TaskStatus status;
    private String assigneeId;
    private Priority priority;

    private TaskFilter() {}

    public TaskStatus getStatus()   { return status; }
    public Priority getPriority()   { return priority; }
    public String getAssigneeId()   { return assigneeId; }

    // Builder pattern — lets callers compose any subset of filters
    public static class Builder {
        private final TaskFilter taskFilter =  new TaskFilter();

        public Builder assigneeId(String assigneeId) {
            taskFilter.assigneeId = assigneeId;
            return this;
        }

        public Builder priority(Priority priority) {
            taskFilter.priority = priority;
            return this;
        }

        public Builder status(TaskStatus status) {
            taskFilter.status = status;
            return this;
        }

        public TaskFilter build() {
            return taskFilter;
        }
    }
}
