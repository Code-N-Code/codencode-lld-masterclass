package amazon.service;

import amazon.enums.PaymentStatus;
import amazon.strategy.PaymentStrategy;

public class PaymentProcessor {
    // Delegates to the injected strategy (Open/Closed Principle)
    public PaymentStatus process(PaymentStrategy strategy, double amount) {
        return strategy.pay(amount);
    }
}
