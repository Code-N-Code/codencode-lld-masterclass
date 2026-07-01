package hotelmanagement.service;

import com.hotel.lld.exception.HotelException;
import com.hotel.lld.model.Booking;
import com.hotel.lld.model.BookingStatus;

public class FreeCancellationPolicy implements CancellationPolicy {

    @Override
    public void validateCancellation(Booking booking) {
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new HotelException("Only confirmed booking can be cancelled");
        }
    }
}