package splitwise.models.expenses;

import splitwise.models.ExpenseMetadata;
import splitwise.models.User;
import splitwise.models.splits.Split;

import java.util.List;


public abstract class Expense {
    private double amount;
    private User paidBy;
    private List<Split> splits;
    private ExpenseMetadata metadata;

    public Expense(double amount, User paidBy, List<Split> splits, ExpenseMetadata metadata) {
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.metadata = metadata;
    }

    public double getAmount() { return amount; }
    public User getPaidBy() { return paidBy; }
    public List<Split> getSplits() { return splits; }
    public ExpenseMetadata getMetadata() { return metadata; }

    // Enforces subclasses to implement their own validation
    public abstract boolean validate();
}
