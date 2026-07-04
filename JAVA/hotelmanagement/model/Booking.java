package hotelmanagement.model;


import hotelmanagement.exception.HotelException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Booking {

    private final String id;
    private final String guestName;
    private final String roomTypeId;
    private final int quantity;
    private final DateRange dateRange;
    private final BigDecimal totalAmount;

    private BookingStatus status;

    public Booking(
            String id,
            String guestName,
            String roomTypeId,
            int quantity,
            DateRange dateRange,
            BigDecimal totalAmount
    ) {
        if (id == null || id.isBlank()) {
            throw new HotelException("Booking id cannot be empty");
        }

        if (guestName == null || guestName.isBlank()) {
            throw new HotelException("Guest name cannot be empty");
        }

        if (roomTypeId == null || roomTypeId.isBlank()) {
            throw new HotelException("Room type id cannot be empty");
        }

        if (quantity <= 0) {
            throw new HotelException("Quantity must be positive");
        }

        if (dateRange == null) {
            throw new HotelException("Date range cannot be null");
        }

        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new HotelException("Total amount cannot be negative");
        }

        this.id = id;
        this.guestName = guestName;
        this.roomTypeId = roomTypeId;
        this.quantity = quantity;
        this.dateRange = dateRange;
        this.totalAmount = totalAmount;
        this.status = BookingStatus.CONFIRMED;
    }

    public String getId() {
        return id;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public synchronized BookingStatus getStatus() {
        return status;
    }

    public synchronized void cancel() {
        if (status != BookingStatus.CONFIRMED) {
            throw new HotelException("Only confirmed booking can be cancelled");
        }

        status = BookingStatus.CANCELLED;
    }

    public synchronized void checkIn(LocalDate businessDate) {
        if (status != BookingStatus.CONFIRMED) {
            throw new HotelException("Only confirmed booking can be checked in");
        }

        if (!dateRange.checkIn().equals(businessDate)) {
            throw new HotelException("Check-in is allowed only on check-in date");
        }

        status = BookingStatus.CHECKED_IN;
    }

    public synchronized void checkOut() {
        if (status != BookingStatus.CHECKED_IN) {
            throw new HotelException("Only checked-in booking can be checked out");
        }

        status = BookingStatus.CHECKED_OUT;
    }

    @Override
    public synchronized String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", guestName='" + guestName + '\'' +
                ", roomTypeId='" + roomTypeId + '\'' +
                ", quantity=" + quantity +
                ", checkIn=" + dateRange.checkIn() +
                ", checkOut=" + dateRange.checkOut() +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }
}