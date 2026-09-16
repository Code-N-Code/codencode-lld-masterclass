package bookmyshow.services;

import bookmyshow.models.Booking;

public class PaymentService {

    public boolean processPayment(Booking booking) {
        // Simple mock payment gateway integration
        System.out.println("Payment for amount " + booking.getTotalAmount() + " successful.");
        return true;
    }
}
