package atm.state;

import atm.core.ATM;
import atm.strategy.AuthStrategy;

public class WaitingForUpiScanState implements ATMState {
    private final ATM atm;

    public WaitingForUpiScanState(ATM atm) { this.atm = atm; }

    @Override public void initiateTransaction(AuthStrategy a) { System.out.println("Session already active."); }

    @Override
    public void provideCredentials(String upiId) {
        if (atm.getAuthStrategy().authenticate(upiId)) {
            System.out.println("UPI Scan Successful.");
            atm.setState(atm.getAuthenticatedState());
        } else {
            System.out.println("UPI Scan Failed/Timeout.");
            endSession();
        }
    }

    @Override public void requestCash(int a) { System.out.println("Please scan QR first."); }

    @Override
    public void endSession() {
        System.out.println("Clearing QR Code... Returning to Idle.");
        atm.setAuthStrategy(null);
        atm.setState(atm.getIdleState());
    }
}
