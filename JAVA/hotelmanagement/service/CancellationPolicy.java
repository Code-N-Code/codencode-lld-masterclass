package hotelmanagement.service;

import com.hotel.lld.model.Booking;

public interface CancellationPolicy {

    void validateCancellation(Booking booking);
}