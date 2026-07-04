package splitwise.factories;

import splitwise.models.ExpenseMetadata;
import splitwise.models.ExpenseType;
import splitwise.models.User;
import splitwise.models.expenses.EqualExpense;
import splitwise.models.expenses.ExactExpense;
import splitwise.models.expenses.Expense;
import splitwise.models.expenses.PercentExpense;
import splitwise.models.splits.PercentSplit;
import splitwise.models.splits.Split;

import java.util.List;

public class ExpenseFactory {
    public static Expense createExpense(ExpenseType type, double amount, User paidBy, List<Split> splits, ExpenseMetadata metadata) {
        switch (type) {
            case EQUAL:
                int totalSplits = splits.size();
                double splitAmount = ((double) Math.round(amount * 100 / totalSplits)) / 100.0;

                for (Split split : splits) {
                    split.setAmount(splitAmount);
                }
                // Handle fractional remainders
                splits.getFirst().setAmount(splitAmount + (amount - splitAmount * totalSplits));
                return new EqualExpense(amount, paidBy, splits, metadata);

            case EXACT:
                return new ExactExpense(amount, paidBy, splits, metadata);

            case PERCENT:
                for (Split split : splits) {
                    PercentSplit percentSplit = (PercentSplit) split;
                    split.setAmount((amount * percentSplit.getPercent()) / 100.0);
                }
                return new PercentExpense(amount, paidBy, splits, metadata);

            default:
                throw new IllegalArgumentException("Invalid Expense Type");
        }
    }
}
