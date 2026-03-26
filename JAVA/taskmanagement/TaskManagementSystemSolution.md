# Task Management System — Low Level Design (LLD)

> A complete Java implementation of a Task Management System, built as a teaching resource for Low Level Design interviews.

---

## 📺 Course Resources

| Resource | Link                                                                                 |
|---|--------------------------------------------------------------------------------------|
| YouTube Channel | [CodeNCode](https://www.youtube.com/@codencode)                                         |
| Full LLD Course | [Low Level Design - YouTube](https://www.youtube.com/playlist?list=Low+Level+Design) |
| This Lecture | [Task Management System — LLD](https://youtu.be/rEPNruXeI0U)                         |

---

## 📁 Project Structure

```
taskmanagement/
├── demo/
│   └── TaskManagementDemo.java       ← Run this to see everything in action
├── dto/
│   └── TaskFilter.java               ← Query filter (Builder pattern)
├── enums/
│   ├── Priority.java                 ← LOW, MEDIUM, HIGH
│   ├── TaskStatus.java               ← TODO, IN_PROGRESS, COMPLETED
│   └── TaskEventType.java            ← CREATED, ASSIGNED, STATUS_CHANGED
├── model/
│   ├── Task.java                     ← Core domain entity
│   └── User.java                     ← User identity holder
├── notification/
│   ├── TaskObserver.java             ← Observer interface
│   ├── NotificationService.java      ← Observable subject
│   └── EmailNotifier.java            ← Concrete observer
├── repository/
│   ├── TaskRepository.java           ← Interface (contract)
│   ├── InMemoryTaskRepository.java   ← Implementation using ConcurrentHashMap
│   ├── UserRepository.java           ← Interface (contract)
│   └── InMemoryUserRepository.java   ← Implementation using ConcurrentHashMap
└── service/
    └── TaskService.java              ← Business logic orchestrator
```

---

## 🧠 Design Overview

This system is intentionally scoped for a **45-minute LLD interview**. It covers:

- Clean package structure by feature/concern
- Domain model with business rule enforcement
- Repository pattern for storage abstraction
- Observer pattern for notifications
- Thread-safe concurrent access

---

## 📦 Package-by-Package Walkthrough

### `enums/` — Type Safety First

Three enums that eliminate magic strings from the codebase.

**`TaskStatus.java`**
```java
public enum TaskStatus {
    TODO, IN_PROGRESS, COMPLETED
}
```
Represents the lifecycle of a task. A task always starts as `TODO` and can only move forward — it can never go back once `COMPLETED`.

**`Priority.java`**
```java
public enum Priority {
    LOW, MEDIUM, HIGH
}
```

**`TaskEventType.java`**
```java
public enum TaskEventType {
    CREATED, ASSIGNED, STATUS_CHANGED
}
```
Used by the notification system to tell observers *what happened* to a task.

> 💡 **Interview tip**: Define enums before anything else. It signals you think about type safety — no raw strings flying around.

---

### `model/` — Domain Entities

These are the real-world things your system manages. They have identity, lifecycle, and business rules.

**`Task.java`** — the most important class in the system.

```java
public class Task {
    private final String taskId;   // immutable — set once at creation
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private String assigneeId;
    private LocalDate dueDate;

    public Task(String title, String description, Priority priority, LocalDate dueDate) {
        this.taskId = UUID.randomUUID().toString();
        this.status = TaskStatus.TODO;  // always starts as TODO
        ...
    }

    public void setStatus(TaskStatus status) {
        if (this.status == TaskStatus.COMPLETED) {
            throw new IllegalStateException("Cannot change status of a completed task.");
        }
        this.status = status;
    }
}
```

Key design decisions:
- `taskId` is `final` — once created, the ID never changes
- `setStatus()` is **not** a plain setter — it enforces the state machine
- A `COMPLETED` task is terminal — no further transitions are allowed

> 💡 **Interview tip**: This is where you discuss the **State Machine** pattern. The model enforces its own rules. Business logic belongs here, not scattered across callers.

**`User.java`** — a plain identity holder.

```java
public class User {
    private final String userId;
    private final String name;
    private final String email;
}
```
`User` has zero opinion about tasks. It only knows who a person is.

---

### `dto/` — Data Transfer Objects

**`TaskFilter.java`** — a throwaway query descriptor, not a domain entity.

```java
TaskFilter filter = new TaskFilter.Builder()
    .status(TaskStatus.IN_PROGRESS)
    .priority(Priority.HIGH)
    .assigneeId(alice.getUserId())
    .build();
```

Uses the **Builder pattern** so callers can compose any combination of filters without a constructor explosion. Key point: this class has no identity, no lifecycle, and is never persisted — that's why it lives in `dto/` and not `model/`.

> 💡 **A useful test**: *"Does this object get saved anywhere?"* If no — it belongs in `dto/`, not `model/`.

---

### `repository/` — Storage Abstraction

The repository pattern draws a clean line between business logic and storage. The service layer talks to the *interface*, never to a specific implementation.

**`TaskRepository.java`** — the contract:
```java
public interface TaskRepository {
    void save(Task task);
    Optional<Task> findById(String id);
    List<Task> findAll(TaskFilter filter);
    void deleteById(String taskId);
}
```

**`InMemoryTaskRepository.java`** — the only class that knows tasks live in a `ConcurrentHashMap`:

```java
public class InMemoryTaskRepository implements TaskRepository {
    private final Map<String, Task> tasks = new ConcurrentHashMap<>();

    @Override
    public List<Task> findAll(TaskFilter filter) {
        return tasks.values().stream()
            .filter(t -> filter.getAssigneeId() == null || t.getAssigneeId().equals(filter.getAssigneeId()))
            .filter(t -> filter.getPriority()   == null || t.getPriority().equals(filter.getPriority()))
            .filter(t -> filter.getStatus()     == null || t.getStatus().equals(filter.getStatus()))
            .collect(Collectors.toList());
    }
}
```

**Why `ConcurrentHashMap` and not `HashMap`?**

| | `HashMap` | `Hashtable` | `ConcurrentHashMap` |
|---|---|---|---|
| Thread-safe? | ❌ No | ✅ Yes | ✅ Yes |
| Locking strategy | None | Full-table lock | Bucket-level lock |
| Concurrent reads | Unsafe | Blocks | Lock-free (Java 8+) |
| Null keys/values | Allowed | Not allowed | Not allowed |

`ConcurrentHashMap` is the right default for any in-memory store accessed by multiple threads. Threads operating on **different buckets** run truly in parallel. (Note: it's bucket-level, not key-level — two keys that hash to the same bucket will still contend.)

**`UserRepository.java`** follows the same interface + implementation split:
```java
public interface UserRepository {
    void save(User user);
    Optional<User> findById(String id);
}
```

> 💡 **Why use interfaces for both?** If tomorrow you swap in-memory storage for a database, `TaskService` doesn't change at all. This is the **Dependency Inversion Principle** — depend on abstractions, not concretions.

---

### `notification/` — Observer Pattern

The notification system is deliberately isolated in its own package. If you rip it out or swap it for Kafka, you touch exactly one package.

**`TaskObserver.java`** — the contract every notification channel must fulfill:
```java
public interface TaskObserver {
    void onTaskEvent(Task task, TaskEventType eventType);
}
```

**`NotificationService.java`** — the Observable subject that manages observers:

```java
public class NotificationService {
    private final List<TaskObserver> observers = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void addObserver(TaskObserver observer) { observers.add(observer); }
    public void removeObserver(TaskObserver observer) { observers.remove(observer); }

    public void notifyObservers(Task task, TaskEventType eventType) {
        for (TaskObserver observer : observers) {
            try {
                observer.onTaskEvent(task, eventType);
            } catch (Exception e) {
                System.err.println("Notification failed: " + e.getMessage());
            }
        }
    }
}
```

**Why `CopyOnWriteArrayList`?**

The observer list is read very frequently (on every task event) but written rarely (only when adding/removing notifiers). `CopyOnWriteArrayList` is optimised for exactly this pattern:
- Reads iterate over a snapshot — completely lock-free
- Writes create a fresh copy of the backing array

This avoids `ConcurrentModificationException` when iterating and notifying simultaneously.


**`EmailNotifier.java`** — a concrete observer:
```java
public class EmailNotifier implements TaskObserver {
    @Override
    public void onTaskEvent(Task task, TaskEventType eventType) {
        System.out.println("[EMAIL] Event: " + eventType + " | Task: " + task.getTitle());
    }
}
```

You can also add observers as lambdas — this demonstrates the **Strategy pattern** in action:
```java
notificationService.addObserver((task, eventType) ->
    System.out.println("[SMS] Event: " + eventType + " | Task: " + task.getTitle()));
```

Adding a new notification channel (Slack, webhook, push notification) requires **zero changes** to `NotificationService`. That is the Open/Closed Principle.

---

### `service/` — The Orchestrator

**`TaskService.java`** — coordinates all the other components. It owns no data, enforces no storage rules, and has no knowledge of HTTP or databases. Its only job is: fetch → validate → mutate → persist → notify.

```java
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public Task createTask(String title, String description,
                           Priority priority, LocalDate dueDate) {
        Task task = new Task(title, description, priority, dueDate);
        taskRepository.save(task);
        notificationService.notifyObservers(task, TaskEventType.CREATED);
        return task;
    }

    public Task updateStatus(String taskId, TaskStatus status) {
        Task task = findTaskOrThrow(taskId);
        synchronized (task) {              // per-entity lock
            task.setStatus(status);
            taskRepository.save(task);
        }
        notificationService.notifyObservers(task, TaskEventType.STATUS_CHANGED);
        return task;
    }

    public Task assignTask(String taskId, String assigneeId) {
        Task task = findTaskOrThrow(taskId);
        userRepository.findById(assigneeId)
            .orElseThrow(() -> new IllegalArgumentException("Assignee " + assigneeId + " not found"));
        synchronized (task) {
            task.setAssigneeId(assigneeId);
            taskRepository.save(task);
        }
        notificationService.notifyObservers(task, TaskEventType.ASSIGNED);
        return task;
    }
}
```

---

## 🔒 Multithreading — The Deep Dive

There are three distinct concurrency concerns in this design. In an interview, articulate each separately.

### 1. Mutating a shared `Task` object — `synchronized(task)`

Two threads could call `updateStatus()` and `assignTask()` on the same task simultaneously. Without synchronization, one write could silently overwrite the other.

```java
synchronized (task) {
    task.setStatus(status);
    taskRepository.save(task);
}
```

We lock on the **task instance itself**, not a global lock. This means operations on different tasks never block each other — this is **per-entity locking** and gives much better throughput than a single global lock.

### 2. The task store — `ConcurrentHashMap`

`HashMap` is not thread-safe and can corrupt its internal structure under concurrent modification. `ConcurrentHashMap` uses bucket-level locking so threads operating on different buckets run truly in parallel.

### 3. Observer list — `CopyOnWriteArrayList`

Reads (iterating to notify) happen constantly; writes (adding/removing observers) happen rarely. `CopyOnWriteArrayList` is designed for this — reads never block, writes pay a copy cost that is acceptable given their rarity.

### 4. Async notifications — `ExecutorService`

```java
private final ExecutorService executor = Executors.newFixedThreadPool(5);
```

`newFixedThreadPool(5)` creates exactly 5 worker threads. Tasks submitted via `executor.submit()` go into an internal queue. If all 5 threads are busy, tasks wait in the queue until a thread is free. The threads are never destroyed between tasks — they sit idle waiting.

For notification workloads (I/O-bound — calling email/SMS APIs), a **cached thread pool** or Java 21 **virtual threads** (`Executors.newVirtualThreadPerTaskExecutor()`) would be even better since I/O threads spend most time waiting, not consuming CPU.

---

## 🎨 Design Patterns Used

| Pattern | Where | Why |
|---|---|---|
| **Observer** | `NotificationService` + `TaskObserver` | Decouple task events from notification channels |
| **Repository** | `TaskRepository` / `UserRepository` interfaces | Swap storage without touching business logic |
| **Builder** | `TaskFilter.Builder` | Composable optional query parameters |
| **State Machine** | `Task.setStatus()` | Enforce legal status transitions in the model itself |
| **Strategy** | `EmailNotifier` implementing `TaskObserver` | Plug in new notifiers without changing anything else |
| **Dependency Injection** | `TaskService` constructor | All dependencies passed in, nothing instantiated internally |

---

## ▶️ How to Run

**Prerequisites**: Java 11+, no external dependencies required.

```bash
# Compile from the project root
javac -d out $(find . -name "*.java")

# Run the demo
java -cp out taskmanagement.demo.TaskManagementDemo
```

**Expected output:**
```
=== Users created: Alice, Bob

=== Creating tasks ===
[EMAIL] Event: CREATED | Task: Design DB schema | Assignee: null
[SMS]   Event: CREATED | Task: Design DB schema
...

=== Filter: IN_PROGRESS tasks ===
  - Write unit tests [MEDIUM]

=== State machine guard ===
  Caught expected error: Cannot change status of a completed task.

=== Multithreading: concurrent updates on task2 ===
  Thread-1: moved task2 → DONE
  Thread-2: assigned task2 → Alice
  task2 final status:   COMPLETED
  task2 final assignee: <alice-uuid>

=== Remaining tasks: 2

=== Demo complete ===
```

---

## 📚 What the Demo Covers

The `TaskManagementDemo.java` file walks through 9 sections:

1. **Wire up dependencies** — shows constructor injection, plugging in observers
2. **Create users** — Alice and Bob
3. **Create tasks** — three tasks with different priorities and due dates
4. **Assign tasks** — distributes tasks between Alice and Bob
5. **Update statuses** — moves tasks through the lifecycle
6. **Filter tasks** — demonstrates `TaskFilter.Builder` for status, assignee, and priority filters
7. **State machine guard** — deliberately tries to move a `COMPLETED` task and catches the expected `IllegalStateException`
8. **Multithreading** — two threads race on the same task; `synchronized(task)` ensures no data corruption
9. **Delete** — removes a task and verifies remaining count

> 💡 Section 7 (state machine guard) and Section 8 (multithreading) are the most important for interviews. Run the demo and study what the output tells you about each.

---

## 🔗 Further Learning

- Watch the full lecture: [Task Management System LLD](https://youtu.be/rEPNruXeI0U)
- Explore more LLD problems on the channel: [CodeNCode](https://www.youtube.com/@codencode)
- Full LLD playlist: [Low Level Design - YouTube](https://www.youtube.com/playlist?list=Low+Level+Design)
