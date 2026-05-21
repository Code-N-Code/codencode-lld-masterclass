package atm.strategy;

import atm.core.ATM;
import atm.entity.BankAccount;
import atm.state.ATMState;

public interface AuthStrategy {
    boolean authenticate(String credentials);
    BankAccount getAccount();
    ATMState getNextState(ATM atm);
    String getAuthMethodName();
}
