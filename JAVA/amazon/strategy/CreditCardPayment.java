package amazon.strategy;

import amazon.enums.PaymentStatus;

public class CreditCardPayment implements PaymentStrategy {
    private final String cardNumber;
    private final String cvv;

    public CreditCardPayment(String cardNumber, String cvv) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    @Override
    public PaymentStatus pay(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
        // Integrate with Stripe/Razorpay here
        return PaymentStatus.SUCCESS;
    }
}
