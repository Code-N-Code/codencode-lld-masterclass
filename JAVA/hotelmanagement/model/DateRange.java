package hotelmanagement.model;

import hotelmanagement.exception.HotelException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public record DateRange(LocalDate checkIn, LocalDate checkOut) {

    public DateRange {
        if (checkIn == null || checkOut == null) {
            throw new HotelException("Check-in and check-out dates cannot be null");
        }

        if (!checkIn.isBefore(checkOut)) {
            throw new HotelException("Check-in date must be before check-out date");
        }
    }

    public long numberOfNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public List<LocalDate> getNights() {
        List<LocalDate> nights = new ArrayList<>();

        LocalDate current = checkIn;

        while (current.isBefore(checkOut)) {
            nights.add(current);
            current = current.plusDays(1);
        }

        return nights;
    }
}