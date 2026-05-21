package atm.state;

import atm.strategy.AuthStrategy;

public interface ATMState {
    void initiateTransaction(AuthStrategy authStrategy);
    void provideCredentials(String credentials);
    void requestCash(int amount);
    void endSession();
}
