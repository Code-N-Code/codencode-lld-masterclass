package taskmanagement.service;

import taskmanagement.dto.TaskFilter;
import taskmanagement.enums.Priority;
import taskmanagement.enums.TaskEventType;
import taskmanagement.enums.TaskStatus;
import taskmanagement.model.Task;
import taskmanagement.notification.NotificationService;
import taskmanagement.repository.TaskRepository;
import taskmanagement.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public Task createTask(String title, String description,
                           Priority priority, LocalDate dueDate) {
        Task task = new Task(title, description, priority, dueDate);
        taskRepository.save(task);
        notificationService.notifyObservers(task, TaskEventType.CREATED);
        return task;
    }

    public Task updateStatus(String taskId, TaskStatus status) {
        Task task = findTaskOrThrow(taskId);
        // Synchronized on the task object itself to prevent race conditions
        synchronized (task) {
            task.setStatus(status);
            taskRepository.save(task);
        }
        notificationService.notifyObservers(task, TaskEventType.STATUS_CHANGED);
        return task;
    }

    public Task assignTask(String taskId, String assigneeId) {
        Task task = findTaskOrThrow(taskId);

        // Validate assignee exists
        userRepository.findById(assigneeId)
                .orElseThrow(() -> new IllegalArgumentException("Assignee " + assigneeId + " not found"));

        synchronized (task) {
            task.setAssigneeId(assigneeId);
            taskRepository.save(task);
        }

        notificationService.notifyObservers(task, TaskEventType.ASSIGNED);
        return task;
    }

    public List<Task> getTasks(TaskFilter filter) {
        return taskRepository.findAll(filter);
    }

    public void deleteTask(String taskId) {
        findTaskOrThrow(taskId);
        taskRepository.deleteById(taskId);
    }

    private Task findTaskOrThrow(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
    }
}
