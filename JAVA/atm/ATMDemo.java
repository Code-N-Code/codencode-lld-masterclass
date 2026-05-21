package atm;

import atm.core.ATM;
import atm.entity.Card;
import atm.hardware.CashDispenser;
import atm.service.BankService;
import atm.strategy.AuthStrategy;
import atm.strategy.DebitCardAuth;
import atm.strategy.UpiAuth;

public class ATMDemo {
    public static void main(String[] args) {
        System.out.println("=== BOOTING UP ATM SYSTEM ===");
        BankService bankService = new BankService();
        CashDispenser cashDispenser = new CashDispenser(); // Loaded with ₹7400 total
        ATM atm = new ATM(bankService, cashDispenser);

        System.out.println("\n=== SCENARIO 1: TRADITIONAL DEBIT CARD WITHDRAWAL ===");
        Card myCard = new Card("CARD-123");
        AuthStrategy cardStrategy = new DebitCardAuth(myCard, bankService);

        atm.initiateTransaction(cardStrategy);
        atm.provideCredentials("4321"); // Correct PIN
        atm.requestCash(1680); // Needs 500x3, 100x1, 50x1, 20x1, 10x1


        System.out.println("\n=== SCENARIO 2: CARDLESS UPI WITHDRAWAL ===");
        AuthStrategy upiStrategy = new UpiAuth(bankService);

        atm.initiateTransaction(upiStrategy);
        atm.provideCredentials("user@upi"); // Simulating successful App scan
        atm.requestCash(300); // Needs 100x3


        System.out.println("\n=== SCENARIO 3: CONCURRENCY TEST (RACE CONDITION PREVENTION) ===");
        // The account started with ₹5000.
        // We withdrew 1680 + 300 = ₹1980. Remaining balance should be ₹3020.
        // Let's fire two simultaneous threads trying to withdraw ₹3000 each.
        // Only ONE should succeed.

        Runnable concurrentWithdrawal = () -> {
            // Using UPI strategy directly for the thread simulation
            AuthStrategy threadStrategy = new UpiAuth(bankService);
            ATM threadAtm = new ATM(bankService, cashDispenser);

            System.out.println(Thread.currentThread().getName() + " attempting ₹3000 withdrawal...");
            threadAtm.initiateTransaction(threadStrategy);
            threadAtm.provideCredentials("user@upi");
            threadAtm.requestCash(3000);
        };

        Thread t1 = new Thread(concurrentWithdrawal, "[ATM Machine A]");
        Thread t2 = new Thread(concurrentWithdrawal, "[Online Auto-Pay]");

        t1.start();
        t2.start();
    }
}
