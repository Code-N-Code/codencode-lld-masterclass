package hotelmanagement.service;

import hotelmanagement.model.Booking;

public interface CancellationPolicy {

    void validateCancellation(Booking booking);
}