package splitwise.models;

import splitwise.models.expenses.Expense;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group {
    private String id;
    private String name;
    private String description;
    // Thread-safe lists for concurrent modifications
    private List<User> members;
    private List<Expense> expenses;

    public Group(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.members = new CopyOnWriteArrayList<>();
        this.expenses = new CopyOnWriteArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<User> getMembers() { return members; }
    public List<Expense> getExpenses() { return expenses; }

    public void addMember(User user) {
        // Prevent duplicates
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
}
