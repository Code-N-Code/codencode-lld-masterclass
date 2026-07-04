package splitwise.services;

import splitwise.factories.ExpenseFactory;
import splitwise.models.ExpenseMetadata;
import splitwise.models.ExpenseType;
import splitwise.models.Group;
import splitwise.models.User;
import splitwise.models.expenses.Expense;
import splitwise.models.splits.Split;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SplitwiseService {
    private final UserService userService;
    private final GroupService groupService;
    // Map of User -> (Map of OwedUser -> Amount)
    private final Map<String, Map<String, Double>> globalBalanceSheet;

    public SplitwiseService(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
        this.globalBalanceSheet = new ConcurrentHashMap<>();
    }

    public void addUser(User user) {
        userService.addUser(user);
        globalBalanceSheet.put(user.getId(), new ConcurrentHashMap<>());
    }

    public User getUser(String userId) {
        return userService.getUser(userId);
    }

    public void createGroup(String groupId, String name, String desc) {
        groupService.createGroup(groupId, name, desc);
    }

    public void addUserToGroup(String userId, String groupId) {
        User user = userService.getUser(userId);
        groupService.addUserToGroup(groupId, user);
    }

    public void addExpense(String groupId, ExpenseType type, double amount, String paidBy, List<Split> splits, ExpenseMetadata metadata) {
        User user = userService.getUser(paidBy);
        Expense expense = ExpenseFactory.createExpense(type, amount, user, splits, metadata);

        if (!expense.validate()) {
            System.out.println("Invalid expense splits provided for: " + metadata.getName());
            return;
        }

        // Handle Group Logic if a groupId is provided
        if (groupId != null && groupService.contains(groupId)) {
            Group group = groupService.getGroup(groupId);

            // Validate all users in the split belong to the group
            for (Split split : splits) {
                if (!group.getMembers().contains(split.getUser())) {
                    System.out.println("Error: User " + split.getUser().getName() + " is not a member of group " + group.getName());
                    return;
                }
            }
            group.addExpense(expense);
        }

        // Update Global Balances Thread-Safely
        for (Split split : expense.getSplits()) {
            String paidTo = split.getUser().getId();
            double oweAmount = split.getAmount();

            if (!paidBy.equals(paidTo)) {
                globalBalanceSheet.get(paidBy).compute(paidTo, (k, v) -> (v == null ? 0 : v) + oweAmount);
                globalBalanceSheet.get(paidTo).compute(paidBy, (k, v) -> (v == null ? 0 : v) - oweAmount);
            }
        }
        System.out.println("Successfully added expense: " + metadata.getName());
    }

    public void showBalances() {
        boolean isEmpty = true;
        System.out.println("\n--- Global Balance Sheet ---");
        for (Map.Entry<String, Map<String, Double>> allBalances : globalBalanceSheet.entrySet()) {
            for (Map.Entry<String, Double> userBalance : allBalances.getValue().entrySet()) {
                if (userBalance.getValue() > 0) {
                    isEmpty = false;
                    printBalance(allBalances.getKey(), userBalance.getKey(), userBalance.getValue());
                }
            }
        }
        if (isEmpty) System.out.println("No balances in the system.");
        System.out.println("----------------------------\n");
    }

    private void printBalance(String user1, String user2, double amount) {
        String name1 = userService.getUser(user1).getName();
        String name2 = userService.getUser(user2).getName();
        System.out.println(name2 + " owes " + name1 + ": $" + Math.abs(amount));
    }
}
