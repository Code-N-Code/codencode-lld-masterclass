package hotelmanagement.service;

import hotelmanagement.exception.HotelException;
import hotelmanagement.model.Booking;
import hotelmanagement.model.BookingStatus;

public class FreeCancellationPolicy implements CancellationPolicy {

    @Override
    public void validateCancellation(Booking booking) {
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new HotelException("Only confirmed booking can be cancelled");
        }
    }
}