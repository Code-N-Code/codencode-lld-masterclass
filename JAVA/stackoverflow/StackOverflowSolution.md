# Stack Overflow — Low Level Design (LLD)

> A complete Java implementation of a Stack Overflow-like Q&A platform, built as a teaching resource for Low Level Design interviews.

---

## 📺 Course Resources

| Resource | Link |
|---|---|
| YouTube Channel | [CodeNCode](https://www.youtube.com/@codencode) |
| Full LLD Course | [Low Level Design — YouTube Playlist](https://www.youtube.com/playlist?list=Low+Level+Design) |
| ⭐ Full Source Code | [codencode-lld-masterclass / JAVA / stackoverflow](https://github.com/Code-N-Code/codencode-lld-masterclass/tree/main/JAVA/stackoverflow) |

---

## 📁 Project Structure

```
stackoverflow/
├── demo/
│   └── MainDemo.java                       ← Run this to see everything in action
├── dto/
│   └── Filter.java                         ← Search filter (Builder pattern)
├── enums/
│   └── VoteType.java                       ← UPVOTE(+1), DOWNVOTE(-1)
├── model/
│   ├── Post.java                           ← Abstract base class (id, content, authorId)
│   ├── Question.java                       ← Extends Post; holds answers, tags, votes
│   ├── Answer.java                         ← Extends Post; thread-safe vote count
│   ├── Comment.java                        ← Extends Post; lightweight content unit
│   ├── Tag.java                            ← Value object with equals/hashCode on name
│   └── User.java                           ← Identity + atomic reputation tracker
├── repository/
│   ├── QuestionRepository.java             ← Interface (contract)
│   ├── InMemoryQuestionRepository.java     ← Implementation using ConcurrentHashMap
│   ├── UserRepository.java                 ← Interface (contract)
│   └── InMemoryUserRepository.java         ← Implementation using ConcurrentHashMap
└── service/
    ├── UserService.java                    ← User creation and reputation management
    ├── QuestionService.java                ← Q&A retrieval, filtering, and voting
    └── StackOverflowService.java           ← Facade — single public API for all operations
```

---

## 🧠 Design Overview

This system is intentionally scoped for a **45-minute LLD interview**. It covers:

- Abstract base class (`Post`) shared by `Question`, `Answer`, and `Comment`
- Facade pattern via `StackOverflowService` — one entry point for all operations
- Repository pattern for storage abstraction
- Thread-safe concurrent voting using `AtomicInteger` and `CopyOnWriteArrayList`
- Composable search via the Builder pattern on `Filter`

---

## 🎨 Design Patterns Used

| Pattern | Where | Why |
|---|---|---|
| **Facade** | `StackOverflowService` | Single entry point; hides the UserService / QuestionService split from callers |
| **Repository** | `QuestionRepository` / `UserRepository` interfaces | Swap storage without touching business logic |
| **Builder** | `Filter.Builder` | Composable optional search parameters without constructor explosion |
| **Inheritance** | `Post` → `Question`, `Answer`, `Comment` | Shared identity and content fields in one place |
| **Value Object** | `Tag` | Equality based on name, not reference — safe to use in `Set<Tag>` |
| **Dependency Injection** | All service constructors | All dependencies passed in, nothing instantiated internally |

---

## 🔒 Thread Safety at a Glance

| Concern | Solution | Why |
|---|---|---|
| Vote counts on `Question` / `Answer` | `AtomicInteger` | Lock-free increment/decrement under high concurrency |
| User reputation updates | `AtomicInteger` | Same — concurrent upvotes update reputation without a lock |
| Answer and comment lists | `CopyOnWriteArrayList` | Frequent reads iterate over a snapshot; writes pay a copy cost |
| Shared question / user store | `ConcurrentHashMap` | Bucket-level locking; lock-free reads on Java 8+ |

---

## ▶️ How to Run

**Prerequisites**: Java 11+, no external dependencies.

```bash
# Compile from the project root
javac -d out $(find . -name "*.java")

# Run the demo
java -cp out stackoverflow.demo.MainDemo
```

**Expected output:**
```
--- Starting Stack Overflow LLD Demo ---

Answer: In Java 8, ConcurrentHashMap uses CAS operations...
Author: <bob-uuid>
Vote: 0

Simulating concurrent upvotes...

--- Results ---
Answer: In Java 8, ConcurrentHashMap uses CAS operations...
Author: <bob-uuid>
Vote: 100

Question: How does ConcurrentHashMap work?
Answer by User: Alice_Dev ...: In Java 8, ConcurrentHashMap...
Answer Votes (Should be 100): 100
Bob's Reputation (Should be 100 * 10 = 1000): 1000

Search results for 'concurrency': 2 question(s) found.
```

---

## 📚 What the Demo Covers

`MainDemo.java` walks through 7 sections in order:

1. **Wire up the system** — instantiates repositories, services, and the facade via constructor injection
2. **Create users** — Alice and Bob registered via `StackOverflowService.createUser()`
3. **Ask questions** — Alice posts a Java concurrency question with `Tag` objects; Bob posts another
4. **Post an answer** — Bob answers Alice's question; the `Answer` is attached directly to the `Question`
5. **Concurrent voting** — 100 threads simultaneously upvote Bob's answer; `AtomicInteger` ensures the final count is exactly 100
6. **Reputation check** — verifies Bob's reputation is `100 × 10 = 1000` (10 points per upvote), demonstrating thread-safe reputation updates
7. **Search** — uses `Filter.Builder` to query all questions; demonstrates composable filtering by tag and author

> 💡 Section 5 (concurrent voting) is the most interview-relevant. It demonstrates that 100 simultaneous writes on a shared counter produce a correct result with zero synchronization overhead, purely via `AtomicInteger`.

---

## 📦 Key Implementation Notes

### `Post.java` — shared base via inheritance

```java
public abstract class Post {
    protected final String id;
    protected String content;
    protected final String authorId;
    protected final LocalDateTime creationDate;
}
```

`Question`, `Answer`, and `Comment` all extend `Post`. Identity (`id`, `authorId`, `creationDate`) is set once at construction and never changes — modelling the immutability of posted content.

### `Answer.java` — lock-free voting

```java
private final AtomicInteger voteCount = new AtomicInteger(0);

public void vote(VoteType voteType) {
    voteCount.addAndGet(voteType.getValue()); // +1 or -1, no lock needed
}
```

`AtomicInteger.addAndGet()` is a single CPU-level CAS instruction. 100 threads calling this simultaneously will all succeed without blocking each other.

### `StackOverflowService.java` — reputation on every vote

```java
public void voteAnswer(String authorId, String questionId, String answerId, VoteType voteType) {
    int reputationDelta = (voteType == VoteType.UPVOTE) ? 10 : -2;
    userService.updateReputation(authorId, reputationDelta); // atomic update on User
    questionService.voteAnswer(questionId, answerId, voteType); // atomic update on Answer
}
```

Both reputation and vote count update atomically — but independently. The facade keeps this orchestration out of both services.

### `Filter.java` — composable search

```java
Filter filter = new Filter.Builder()
    .tag(new Tag("java"))
    .userId(alice.getId())
    .build();

List<Question> results = system.searchQuestion(filter);
```

Each field is optional — `null` means "match all". Any combination of tag and author filters works without overloaded methods.

---

## 🔗 Further Learning

- 📺 More LLD problems: [CodeNCode on YouTube](https://www.youtube.com/@codencode)
- 📋 Full LLD playlist: [Low Level Design — YouTube](https://www.youtube.com/playlist?list=Low+Level+Design)
- 💻 Explore more solutions in this repo: [codencode-lld-masterclass](https://github.com/Code-N-Code/codencode-lld-masterclass)