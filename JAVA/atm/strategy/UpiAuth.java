package atm.strategy;

import atm.core.ATM;
import atm.entity.BankAccount;
import atm.service.BankService;
import atm.state.ATMState;

public class UpiAuth implements AuthStrategy {
    private final BankService bankService;
    private BankAccount verifiedAccount;

    public UpiAuth(BankService bankService) {
        this.bankService = bankService;
    }

    @Override
    public boolean authenticate(String upiId) {
        if (bankService.verifyUpiScan(upiId)) {
            this.verifiedAccount = bankService.getAccountByUpi(upiId);
            return true;
        }
        return false;
    }

    @Override
    public BankAccount getAccount() {
        return verifiedAccount;
    }

    @Override
    public ATMState getNextState(ATM atm) {
        System.out.println("Please scan the QR code using your UPI App.");
        return atm.getWaitingForUpiScanState();
    }

    @Override
    public String getAuthMethodName() { return "UPI"; }
}
