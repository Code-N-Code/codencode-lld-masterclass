package splitwise.models.expenses;

import splitwise.models.ExpenseMetadata;
import splitwise.models.User;
import splitwise.models.splits.EqualSplit;
import splitwise.models.splits.Split;

import java.util.List;

public class EqualExpense extends Expense {
    public EqualExpense(double amount, User paidBy, List<Split> splits, ExpenseMetadata metadata) {
        super(amount, paidBy, splits, metadata);
    }

    @Override
    public boolean validate() {
        double totalSplitAmount = 0;

        for (Split split : getSplits()) {
            // 1. Validate the type
            if (!(split instanceof EqualSplit)) {
                return false;
            }
            totalSplitAmount += split.getAmount();
        }

        return Math.abs(getAmount() - totalSplitAmount) < 0.0001;
    }
}
