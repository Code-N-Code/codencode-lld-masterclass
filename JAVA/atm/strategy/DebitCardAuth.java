package atm.strategy;

import atm.core.ATM;
import atm.entity.BankAccount;
import atm.entity.Card;
import atm.service.BankService;
import atm.state.ATMState;

public class DebitCardAuth implements AuthStrategy {
    private final Card card;
    private final BankService bankService;

    public DebitCardAuth(Card card, BankService bankService) {
        this.card = card;
        this.bankService = bankService;
    }

    @Override
    public boolean authenticate(String pin) {
        return bankService.verifyCardPin(card.getCardNumber(), pin);
    }

    @Override
    public BankAccount getAccount() {
        return bankService.getAccountByCard(card.getCardNumber());
    }

    @Override
    public ATMState getNextState(ATM atm) {
        System.out.println("Card Inserted. Please enter your PIN.");
        return atm.getWaitingForPinState();
    }

    @Override
    public String getAuthMethodName() { return "Debit Card"; }
}
