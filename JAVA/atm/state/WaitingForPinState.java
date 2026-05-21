package atm.state;

import atm.core.ATM;
import atm.strategy.AuthStrategy;

public class WaitingForPinState implements ATMState {
    private final ATM atm;

    public WaitingForPinState(ATM atm) { this.atm = atm; }

    @Override public void initiateTransaction(AuthStrategy a) { System.out.println("Session already active."); }

    @Override
    public void provideCredentials(String pin) {
        if (atm.getAuthStrategy().authenticate(pin)) {
            System.out.println("PIN Accepted.");
            atm.setState(atm.getAuthenticatedState());
        } else {
            System.out.println("Invalid PIN.");
            endSession();
        }
    }

    @Override public void requestCash(int a) { System.out.println("Please enter PIN first."); }

    @Override
    public void endSession() {
        System.out.println("Ejecting Card... Returning to Idle.");
        atm.setAuthStrategy(null);
        atm.setState(atm.getIdleState());
    }
}
