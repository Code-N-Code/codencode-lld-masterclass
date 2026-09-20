package amazon.strategy;

import amazon.enums.PaymentStatus;

public interface PaymentStrategy {
    PaymentStatus pay(double amount);
}
