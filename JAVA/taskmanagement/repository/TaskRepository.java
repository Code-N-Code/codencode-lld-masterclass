package taskmanagement.repository;

import taskmanagement.dto.TaskFilter;
import taskmanagement.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    void save(Task task);
    Optional<Task> findById(String id);
    List<Task> findAll(TaskFilter filter);
    void deleteById(String taskId);
}
