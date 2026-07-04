package splitwise;

import splitwise.models.ExpenseMetadata;
import splitwise.models.ExpenseType;
import splitwise.models.User;
import splitwise.services.GroupService;
import splitwise.services.SplitwiseService;
import splitwise.models.splits.EqualSplit;
import splitwise.models.splits.ExactSplit;
import splitwise.models.splits.Split;
import splitwise.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class SplitwiseApplicationDemo {
    public static void main(String[] args) {
        SplitwiseService service = new SplitwiseService(new UserService(), new GroupService());

        // 1. Setup Users
        service.addUser(new User("u1", "Alice", "alice@test.com"));
        service.addUser(new User("u2", "Bob", "bob@test.com"));
        service.addUser(new User("u3", "Charlie", "charlie@test.com"));
        service.addUser(new User("u4", "David", "david@test.com"));

        // 2. Setup Group
        service.createGroup("g1", "Goa Trip", "New Year Trip");
        service.addUserToGroup("u1", "g1");
        service.addUserToGroup("u2", "g1");
        service.addUserToGroup("u3", "g1");
        // Note: David (u4) is NOT in the group

        // 3. Scenario A: Add Expense to Group (Alice pays $900 equally for Alice, Bob, Charlie)
        List<Split> groupSplits = new ArrayList<>();
        groupSplits.add(new EqualSplit(service.getUser("u1")));
        groupSplits.add(new EqualSplit(service.getUser("u2")));
        groupSplits.add(new EqualSplit(service.getUser("u3")));

        service.addExpense("g1", ExpenseType.EQUAL, 900, "u1", groupSplits, new ExpenseMetadata("Hotel Booking", null, "Goa"));

        // 4. Scenario B: Try adding an outsider to Group Expense (Will Fail Validation)
        List<Split> invalidGroupSplits = new ArrayList<>();
        invalidGroupSplits.add(new EqualSplit(service.getUser("u1")));
        invalidGroupSplits.add(new EqualSplit(service.getUser("u4"))); // David not in G1
        service.addExpense("g1", ExpenseType.EQUAL, 200, "u1", invalidGroupSplits, new ExpenseMetadata("Drinks", null, "Invalid"));

        // 5. Scenario C: Individual non-group Expense (Bob pays $500 exact for Bob and David)
        List<Split> nonGroupSplits = new ArrayList<>();
        nonGroupSplits.add(new ExactSplit(service.getUser("u2"), 100));
        nonGroupSplits.add(new ExactSplit(service.getUser("u4"), 400));

        // Passing null for groupId
        service.addExpense(null, ExpenseType.EXACT, 500, "u2", nonGroupSplits, new ExpenseMetadata("Flight to Delhi", null, "1-on-1"));

        // 6. Print all global balances
        service.showBalances();
    }
}
