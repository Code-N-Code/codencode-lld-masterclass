package splitwise;

import java.util.*;

public class SplitwiseAlgorithm {

    // Simple container for the edges of our graph (transactions)
    static class Transaction {
        String from;
        String to;
        double amount;

        Transaction(String from, String to, double amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return from + " pays " + to + " $" + String.format("%.2f", amount);
        }
    }

    // Helper class to store a person and their active balance in the heap
    static class Person {
        String name;
        double balance;

        Person(String name, double balance) {
            this.name = name;
            this.balance = balance;
        }
    }

    public static List<Transaction> simplifyDebts(List<Transaction> rawTransactions) {
        // Phase 1: Flatten the graph by calculating net balances for each person
        Map<String, Double> netBalances = new HashMap<>();
        for (Transaction t : rawTransactions) {
            netBalances.put(t.from, netBalances.getOrDefault(t.from, 0.0) - t.amount);
            netBalances.put(t.to, netBalances.getOrDefault(t.to, 0.0) + t.amount);
        }

        // Phase 2: Create Max-Heaps for Creditors and Debtors
        // We use a custom comparator to sort by balance in descending order
        PriorityQueue<Person> creditors = new PriorityQueue<>((a, b) -> Double.compare(b.balance, a.balance));
        PriorityQueue<Person> debtors = new PriorityQueue<>((a, b) -> Double.compare(b.balance, a.balance));

        for (Map.Entry<String, Double> entry : netBalances.entrySet()) {
            double balance = entry.getValue();
            // We use 0.01 to avoid floating-point precision issues
            if (balance > 0.01) {
                creditors.offer(new Person(entry.getKey(), balance));
            } else if (balance < -0.01) {
                // Store debtors with a positive absolute value so the Max-Heap works correctly
                debtors.offer(new Person(entry.getKey(), Math.abs(balance)));
            }
        }

        // Phase 3: The Greedy Settlement
        List<Transaction> optimizedTransactions = new ArrayList<>();

        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            // Extract the largest creditor and largest debtor
            Person creditor = creditors.poll();
            Person debtor = debtors.poll();

            // The amount to settle is the minimum of what is owed and what is due
            double settledAmount = Math.min(creditor.balance, debtor.balance);

            // Record the new optimized transaction
            optimizedTransactions.add(new Transaction(debtor.name, creditor.name, settledAmount));

            // Deduct the settled amount from both
            creditor.balance -= settledAmount;
            debtor.balance -= settledAmount;

            // If either person still has a balance, push them back into their respective heap
            if (creditor.balance > 0.01) {
                creditors.offer(creditor);
            }
            if (debtor.balance > 0.01) {
                debtors.offer(debtor);
            }
        }

        return optimizedTransactions;
    }

    public static void main(String[] args) {
        // Example setup: 5 messy, overlapping transactions
        List<Transaction> raw = Arrays.asList(
                new Transaction("Alice", "Bob", 50),
                new Transaction("Bob", "Charlie", 40),
                new Transaction("Charlie", "Alice", 20),
                new Transaction("Charlie", "David", 30),
                new Transaction("David", "Bob", 10)
        );

        System.out.println("--- Original Graph (5 Edges) ---");
        for (Transaction t : raw) {
            System.out.println(t);
        }

        System.out.println("\n--- Optimized Graph ---");
        List<Transaction> optimized = simplifyDebts(raw);
        for (Transaction t : optimized) {
            System.out.println(t);
        }
    }
}
