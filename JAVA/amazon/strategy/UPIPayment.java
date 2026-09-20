package amazon.strategy;

import amazon.enums.PaymentStatus;

public class UPIPayment implements PaymentStrategy {
    private final String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public PaymentStatus pay(double amount) {
        System.out.println("Processing UPI payment of $" + amount + " for ID: " + upiId);
        return PaymentStatus.SUCCESS;
    }
}
