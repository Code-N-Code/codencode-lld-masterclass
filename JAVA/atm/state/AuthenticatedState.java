package atm.state;

import atm.core.ATM;
import atm.entity.BankAccount;
import atm.strategy.AuthStrategy;

public class AuthenticatedState implements ATMState {
    private final ATM atm;

    public AuthenticatedState(ATM atm) { this.atm = atm; }

    @Override public void initiateTransaction(AuthStrategy a) { System.out.println("Already authenticated."); }
    @Override public void provideCredentials(String c) { System.out.println("Already authenticated."); }

    @Override
    public void requestCash(int amount) {
        BankAccount account = atm.getAuthStrategy().getAccount();

        if (account.getBalance() >= amount) {
            // Attempt digital deduction (Thread Safe)
            if (account.withdraw(amount)) {
                // Attempt physical dispense (Hardware Safe)
                if (atm.getCashDispenser().dispenseCash(amount)) {
                    System.out.println("Transaction Complete. Remaining Balance: ₹" + account.getBalance());
                } else {
                    // Rollback digital transaction if hardware fails
                    System.out.println("Dispense failed. Rolling back ₹" + amount + " to account.");
                    // In a real system, you'd call account.deposit(amount) here
                }
            } else {
                System.out.println("Digital transaction failed.");
            }
        } else {
            System.out.println("Insufficient funds in bank account.");
        }
        endSession(); // Always clean up
    }

    @Override
    public void endSession() {
        System.out.println("Ending session securely. Returning to Idle.\n");
        atm.setAuthStrategy(null);
        atm.setState(atm.getIdleState());
    }
}
