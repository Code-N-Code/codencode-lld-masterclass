package hotelmanagement.domain.policy;

import hotelmanagement.domain.model.Booking;

public interface CancellationPolicy {

    void validateCancellation(Booking booking);
}