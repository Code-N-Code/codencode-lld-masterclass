package hotelmanagement.domain.policy;

import hotelmanagement.exception.HotelException;
import hotelmanagement.domain.model.Booking;
import hotelmanagement.domain.model.BookingStatus;

public class FreeCancellationPolicy implements CancellationPolicy {

    @Override
    public void validateCancellation(Booking booking) {
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new HotelException("Only confirmed booking can be cancelled");
        }
    }
}