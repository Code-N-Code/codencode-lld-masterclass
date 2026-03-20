package taskmanagement.repository;

import taskmanagement.dto.TaskFilter;
import taskmanagement.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryTaskRepository implements TaskRepository {
    // ConcurrentHashMap — thread-safe key discussion point!
    private final Map<String, Task> tasks = new ConcurrentHashMap<>();

    @Override
    public void save(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll(TaskFilter filter) {
        return tasks.values().stream()
                .filter(t -> filter.getAssigneeId() == null || t.getAssigneeId().equals(filter.getAssigneeId()))
                .filter(t -> filter.getPriority() == null || t.getPriority().equals(filter.getPriority()))
                .filter(t -> filter.getStatus() == null || t.getStatus().equals(filter.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String taskId) {
        tasks.remove(taskId);
    }
}
