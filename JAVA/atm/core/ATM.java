package atm.core;

import atm.hardware.CashDispenser;
import atm.service.BankService;
import atm.state.*;
import atm.strategy.AuthStrategy;

public class ATM {
    private final ATMState idleState;
    private final ATMState waitingForPinState;
    private final ATMState waitingForUpiScanState;
    private final ATMState authenticatedState;

    private ATMState currentState;
    private AuthStrategy authStrategy;

    private final BankService bankService;
    private final CashDispenser cashDispenser;

    public ATM(BankService bankService, CashDispenser cashDispenser) {
        this.idleState = new IdleState(this);
        this.waitingForPinState = new WaitingForPinState(this);
        this.waitingForUpiScanState = new WaitingForUpiScanState(this);
        this.authenticatedState = new AuthenticatedState(this);

        this.currentState = idleState;
        this.bankService = bankService;
        this.cashDispenser = cashDispenser;
    }

    // Pass-through state methods
    public void initiateTransaction(AuthStrategy strategy) { currentState.initiateTransaction(strategy); }
    public void provideCredentials(String credentials) { currentState.provideCredentials(credentials); }
    public void requestCash(int amount) { currentState.requestCash(amount); }

    // Getters and Setters
    public void setState(ATMState state) { this.currentState = state; }
    public ATMState getIdleState() { return idleState; }
    public ATMState getWaitingForPinState() { return waitingForPinState; }
    public ATMState getWaitingForUpiScanState() { return waitingForUpiScanState; }
    public ATMState getAuthenticatedState() { return authenticatedState; }

    public void setAuthStrategy(AuthStrategy strategy) { this.authStrategy = strategy; }
    public AuthStrategy getAuthStrategy() { return authStrategy; }

    public BankService getBankService() { return bankService; }
    public CashDispenser getCashDispenser() { return cashDispenser; }
}
