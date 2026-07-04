package hotelmanagement.service;

import hotelmanagement.exception.HotelException;
import hotelmanagement.model.Booking;
import hotelmanagement.model.DateRange;
import hotelmanagement.model.RoomType;
import hotelmanagement.repository.BookingRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

public class BookingService {

    private final RoomCatalog roomCatalog;
    private final InventoryManager inventoryManager;
    private final PricingStrategy pricingStrategy;
    private final CancellationPolicy cancellationPolicy;
    private final BookingRepository bookingRepository;

    private final AtomicLong bookingSequence = new AtomicLong(1);

    public BookingService(
            RoomCatalog roomCatalog,
            InventoryManager inventoryManager,
            PricingStrategy pricingStrategy,
            CancellationPolicy cancellationPolicy,
            BookingRepository bookingRepository
    ) {
        this.roomCatalog = roomCatalog;
        this.inventoryManager = inventoryManager;
        this.pricingStrategy = pricingStrategy;
        this.cancellationPolicy = cancellationPolicy;
        this.bookingRepository = bookingRepository;
    }

    public int searchAvailability(
            String roomTypeId,
            DateRange dateRange
    ) {
        RoomType roomType = roomCatalog.getRoomType(roomTypeId);

        return inventoryManager.getAvailableRooms(roomType, dateRange);
    }

    public Booking book(
            String guestName,
            String roomTypeId,
            int quantity,
            DateRange dateRange
    ) {
        if (guestName == null || guestName.isBlank()) {
            throw new HotelException("Guest name cannot be empty");
        }

        if (quantity <= 0) {
            throw new HotelException("Quantity must be positive");
        }

        RoomType roomType = roomCatalog.getRoomType(roomTypeId);

        BigDecimal totalAmount = pricingStrategy.calculatePrice(
                roomType,
                dateRange,
                quantity
        );

        Booking booking = new Booking(
                generateBookingId(),
                guestName,
                roomTypeId,
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

        RoomType roomType = roomCatalog.getRoomType(booking.getRoomTypeId());

        inventoryManager.release(
                roomType,
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