package atm.service;

import atm.entity.BankAccount;

import java.util.HashMap;
import java.util.Map;

public class BankService {
    private final Map<String, BankAccount> accounts = new HashMap<>();

    // Mappings for different authentication types
    private final Map<String, String> cardToAccountMap = new HashMap<>();
    private final Map<String, String> cardPins = new HashMap<>();

    private final Map<String, String> upiToAccountMap = new HashMap<>();

    public BankService() {
        // Mock Database Initialization
        accounts.put("ACC-1001", new BankAccount("ACC-1001", 5000.00));

        // Link Card to Account
        cardToAccountMap.put("CARD-123", "ACC-1001");
        cardPins.put("CARD-123", "4321");

        // Link UPI to the SAME Account
        upiToAccountMap.put("user@upi", "ACC-1001");
    }

    public boolean verifyCardPin(String cardNumber, String pin) {
        String expectedPin = cardPins.get(cardNumber);
        return expectedPin != null && expectedPin.equals(pin);
    }

    public BankAccount getAccountByCard(String cardNumber) {
        return accounts.get(cardToAccountMap.get(cardNumber));
    }

    public boolean verifyUpiScan(String upiId) {
        // In reality, this pings the UPI gateway to verify a dynamic QR scan.
        // For our simulation, if the ID exists, it's valid.
        return upiToAccountMap.containsKey(upiId);
    }

    public BankAccount getAccountByUpi(String upiId) {
        return accounts.get(upiToAccountMap.get(upiId));
    }
}
