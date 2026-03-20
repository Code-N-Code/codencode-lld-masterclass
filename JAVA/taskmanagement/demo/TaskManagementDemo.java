package taskmanagement.demo;

import taskmanagement.dto.TaskFilter;
import taskmanagement.enums.Priority;
import taskmanagement.enums.TaskStatus;
import taskmanagement.model.Task;
import taskmanagement.model.User;
import taskmanagement.notification.EmailNotifier;
import taskmanagement.notification.NotificationService;
import taskmanagement.repository.InMemoryTaskRepository;
import taskmanagement.repository.InMemoryUserRepository;
import taskmanagement.repository.UserRepository;
import taskmanagement.service.TaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskManagementDemo {

    public static void main(String[] args) throws InterruptedException {

        // ─────────────────────────────────────────
        // 1. WIRE UP DEPENDENCIES
        // ─────────────────────────────────────────
        InMemoryTaskRepository taskRepo = new InMemoryTaskRepository();
        UserRepository userRepo = new InMemoryUserRepository();

        NotificationService notificationService = new NotificationService();
        notificationService.addObserver(new EmailNotifier());   // plug in email
        notificationService.addObserver((task, eventType) ->    // inline SMS notifier (lambda)
                System.out.println("[SMS]   Event: " + eventType
                        + " | Task: " + task.getTitle()));

        TaskService taskService = new TaskService(taskRepo, userRepo, notificationService);

        // ─────────────────────────────────────────
        // 2. CREATE USERS
        // ─────────────────────────────────────────
        User alice = new User("Alice", "alice@example.com");
        User bob   = new User("Bob",   "bob@example.com");
        userRepo.save(alice);
        userRepo.save(bob);
        System.out.println("=== Users created: " + alice.getName() + ", " + bob.getName());

        // ─────────────────────────────────────────
        // 3. CREATE TASKS
        // ─────────────────────────────────────────
        System.out.println("\n=== Creating tasks ===");
        Task task1 = taskService.createTask(
                "Design DB schema",
                "ERD for user and task tables",
                Priority.HIGH,
                LocalDate.now().plusDays(3)
        );

        Task task2 = taskService.createTask(
                "Write unit tests",
                "Cover service layer",
                Priority.MEDIUM,
                LocalDate.now().plusDays(7)
        );

        Task task3 = taskService.createTask(
                "Setup CI pipeline",
                "GitHub Actions config",
                Priority.LOW,
                LocalDate.now().plusDays(14)
        );

        // ─────────────────────────────────────────
        // 4. ASSIGN TASKS
        // ─────────────────────────────────────────
        System.out.println("\n=== Assigning tasks ===");
        taskService.assignTask(task1.getTaskId(), alice.getUserId());
        taskService.assignTask(task2.getTaskId(), bob.getUserId());
        taskService.assignTask(task3.getTaskId(), alice.getUserId());

        // ─────────────────────────────────────────
        // 5. UPDATE STATUSES
        // ─────────────────────────────────────────
        System.out.println("\n=== Updating statuses ===");
        taskService.updateStatus(task1.getTaskId(), TaskStatus.IN_PROGRESS);
        taskService.updateStatus(task2.getTaskId(), TaskStatus.IN_PROGRESS);
        taskService.updateStatus(task1.getTaskId(), TaskStatus.COMPLETED);

        // ─────────────────────────────────────────
        // 6. FILTER TASKS
        // ─────────────────────────────────────────
        System.out.println("\n=== Filter: IN_PROGRESS tasks ===");
        TaskFilter inProgressFilter = new TaskFilter.Builder()
                .status(TaskStatus.IN_PROGRESS)
                .build();
        List<Task> inProgress = taskService.getTasks(inProgressFilter);
        inProgress.forEach(t -> System.out.println("  - " + t.getTitle()
                + " [" + t.getPriority() + "]"));

        System.out.println("\n=== Filter: Alice's tasks ===");
        TaskFilter aliceFilter = new TaskFilter.Builder()
                .assigneeId(alice.getUserId())
                .build();
        List<Task> aliceTasks = taskService.getTasks(aliceFilter);
        aliceTasks.forEach(t -> System.out.println("  - " + t.getTitle()
                + " [" + t.getStatus() + "]"));

        System.out.println("\n=== Filter: HIGH priority tasks ===");
        TaskFilter highPriorityFilter = new TaskFilter.Builder()
                .priority(Priority.HIGH)
                .build();
        List<Task> highPriority = taskService.getTasks(highPriorityFilter);
        highPriority.forEach(t -> System.out.println("  - " + t.getTitle()
                + " [" + t.getStatus() + "]"));

        // ─────────────────────────────────────────
        // 7. STATE MACHINE GUARD DEMO
        //    Try transitioning a DONE task — should throw
        // ─────────────────────────────────────────
        System.out.println("\n=== State machine guard ===");
        try {
            taskService.updateStatus(task1.getTaskId(), TaskStatus.IN_PROGRESS);
        } catch (IllegalStateException e) {
            System.out.println("  Caught expected error: " + e.getMessage());
        }

        // ─────────────────────────────────────────
        // 8. MULTITHREADING DEMO
        //    Two threads race to update the same task
        // ─────────────────────────────────────────
        System.out.println("\n=== Multithreading: concurrent updates on task2 ===");
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        // Thread 1: tries to mark task2 DONE
        threadPool.submit(() -> {
            try {
                taskService.updateStatus(task2.getTaskId(), TaskStatus.COMPLETED);
                System.out.println("  Thread-1: moved task2 → DONE");
            } catch (Exception e) {
                System.out.println("  Thread-1 error: " + e.getMessage());
            }
        });

        // Thread 2: tries to assign task2 to alice (at the same time)
        threadPool.submit(() -> {
            try {
                taskService.assignTask(task2.getTaskId(), alice.getUserId());
                System.out.println("  Thread-2: assigned task2 → Alice");
            } catch (Exception e) {
                System.out.println("  Thread-2 error: " + e.getMessage());
            }
        });

        threadPool.shutdown();
        threadPool.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("  task2 final status:   " + task2.getStatus());
        System.out.println("  task2 final assignee: " + task2.getAssigneeId());

        // ─────────────────────────────────────────
        // 9. CLEANUP
        // ─────────────────────────────────────────
        System.out.println("\n=== Delete task3 ===");
        taskService.deleteTask(task3.getTaskId());
        TaskFilter allFilter = new TaskFilter.Builder().build();
        System.out.println("  Remaining tasks: " + taskService.getTasks(allFilter).size());

        // Give async notifications a moment to print, then shut down
        Thread.sleep(500);
        notificationService.shutdown();
        System.out.println("\n=== Demo complete ===");
    }
}
