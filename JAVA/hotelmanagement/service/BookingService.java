package hotelmanagement.service;

import hotelmanagement.domain.policy.CancellationPolicy;
import hotelmanagement.domain.pricing.PricingStrategy;
import hotelmanagement.exception.HotelException;
import hotelmanagement.domain.model.Booking;
import hotelmanagement.domain.model.DateRange;
import hotelmanagement.domain.model.RoomType;
import hotelmanagement.repository.BookingRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

public class BookingService {

    private final InventoryManager inventoryManager;
    private final PricingStrategy pricingStrategy;
    private final CancellationPolicy cancellationPolicy;
    private final BookingRepository bookingRepository;

    private final AtomicLong bookingSequence = new AtomicLong(1);

    public BookingService(
            InventoryManager inventoryManager,
            PricingStrategy pricingStrategy,
            CancellationPolicy cancellationPolicy,
            BookingRepository bookingRepository
    ) {
        this.inventoryManager = inventoryManager;
        this.pricingStrategy = pricingStrategy;
        this.cancellationPolicy = cancellationPolicy;
        this.bookingRepository = bookingRepository;
    }

    public int searchAvailability(
            RoomType roomType,
            DateRange dateRange
    ) {
        return inventoryManager.getAvailableRooms(roomType, dateRange);
    }

    public Booking book(
            String guestName,
            RoomType roomType,
            int quantity,
            DateRange dateRange
    ) {
        if (guestName == null || guestName.isBlank()) {
            throw new HotelException("Guest name cannot be empty");
        }

        if (quantity <= 0) {
            throw new HotelException("Quantity must be positive");
        }

        BigDecimal totalAmount = pricingStrategy.calculatePrice(
                roomType,
                dateRange,
                quantity
        );

        Booking booking = new Booking(
                generateBookingId(),
                guestName,
                roomType,
                quantity,
                dateRange,
                totalAmount
        );

        inventoryManager.reserve(roomType, dateRange, quantity);

        try {
            bookingRepository.save(booking);
            return booking;
        } catch (RuntimeException exception) {
            inventoryManager.release(roomType, dateRange, quantity);
            throw exception;
        }
    }

    public void cancel(String bookingId) {
        Booking booking = getBooking(bookingId);

        cancellationPolicy.validateCancellation(booking);

        booking.cancel();

        inventoryManager.release(
                booking.getRoomType(),
                booking.getDateRange(),
                booking.getQuantity()
        );
    }

    public void checkIn(String bookingId, LocalDate businessDate) {
        Booking booking = getBooking(bookingId);

        booking.checkIn(businessDate);
    }

    public void checkOut(String bookingId) {
        Booking booking = getBooking(bookingId);

        booking.checkOut();
    }

    public Booking getBooking(String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new HotelException(
                        "Booking not found: " + bookingId
                ));
    }

    private String generateBookingId() {
        return "B" + bookingSequence.getAndIncrement();
    }
}
