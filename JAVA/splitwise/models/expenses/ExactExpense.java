package splitwise.models.expenses;

import splitwise.models.ExpenseMetadata;
import splitwise.models.User;
import splitwise.models.splits.ExactSplit;
import splitwise.models.splits.Split;

import java.util.List;

public class ExactExpense extends Expense {
    public ExactExpense(double amount, User paidBy, List<Split> splits, ExpenseMetadata metadata) {
        super(amount, paidBy, splits, metadata);
    }

    @Override
    public boolean validate() {
        double totalAmount = 0;
        for (Split split : getSplits()) {
            if (!(split instanceof ExactSplit)) return false;
            totalAmount += split.getAmount();
        }
        return Math.abs(getAmount() - totalAmount) < 0.0001;
    }
}
