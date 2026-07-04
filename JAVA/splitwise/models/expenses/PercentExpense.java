package splitwise.models.expenses;

import splitwise.models.ExpenseMetadata;
import splitwise.models.User;
import splitwise.models.splits.PercentSplit;
import splitwise.models.splits.Split;

import java.util.List;

public class PercentExpense extends Expense {
    public PercentExpense(double amount, User paidBy, List<Split> splits, ExpenseMetadata metadata) {
        super(amount, paidBy, splits, metadata);
    }

    @Override
    public boolean validate() {
        double totalPercent = 0;
        for (Split split : getSplits()) {
            if (!(split instanceof PercentSplit)) return false;
            totalPercent += ((PercentSplit) split).getPercent();
        }
        return Math.abs(100.0 - totalPercent) < 0.0001;
    }
}
