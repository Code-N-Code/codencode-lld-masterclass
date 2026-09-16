package bookmyshow.services;

import bookmyshow.enums.BookingStatus;
import bookmyshow.enums.SeatStatus;
import bookmyshow.exceptions.InvalidBookingStateException;
import bookmyshow.exceptions.SeatUnavailableException;
import bookmyshow.models.*;
import bookmyshow.providers.SeatLockProvider;
import bookmyshow.strategies.PricingStrategy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingService {
    private final SeatLockProvider seatLockProvider;
    private final PricingStrategy pricingStrategy;
    private final PaymentService paymentService;

    public BookingService(SeatLockProvider seatLockProvider,
                          PricingStrategy pricingStrategy,
                          PaymentService paymentService) {
        this.seatLockProvider = seatLockProvider;
        this.pricingStrategy = pricingStrategy;
        this.paymentService = paymentService;
    }

    public Booking createBooking(User user, Show show, List<ShowSeat> showSeats) {
        // Step 1: Ensure seats are not permanently booked
        for (ShowSeat showSeat : showSeats) {
            if (showSeat.getStatus() == SeatStatus.BOOKED) {
                throw new SeatUnavailableException("Seat " + showSeat.getSeat().getId() + " is already booked.");
            }
        }

        // Step 2: Extract physical seats and acquire dynamic lock
        List<Seat> physicalSeats = new ArrayList<>();
        for (ShowSeat showSeat : showSeats) {
            physicalSeats.add(showSeat.getSeat());
        }

        seatLockProvider.lockSeats(show, physicalSeats, user);

        // Step 3: Calculate dynamic pricing via strategy
        double totalAmount = pricingStrategy.calculatePrice(showSeats);

        // Step 4: Construct pending booking
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID().toString());
        booking.setUser(user);
        booking.setShow(show);
        booking.setSeats(showSeats);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(Instant.now());

        return booking;
    }

    public Booking confirmPaymentAndBooking(Booking booking, User user) {
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingStateException("Cannot pay for a booking that is not PENDING.");
        }

        // Step 1: Validate lock retention
        for (ShowSeat showSeat : booking.getSeats()) {
            if (!seatLockProvider.validateLock(booking.getShow(), showSeat.getSeat(), user)) {
                booking.setStatus(BookingStatus.EXPIRED);
                throw new SeatUnavailableException("Lock expired for seat " + showSeat.getSeat().getId() + ". Please retry.");
            }
        }

        // Step 2: Process payment
        boolean paymentSuccess = paymentService.processPayment(booking);

        // Step 3: Mutate states based on outcome
        List<Seat> physicalSeats = new ArrayList<>();
        for (ShowSeat showSeat : booking.getSeats()) {
            physicalSeats.add(showSeat.getSeat());
        }

        if (paymentSuccess) {
            booking.setStatus(BookingStatus.CONFIRMED);
            for (ShowSeat showSeat : booking.getSeats()) {
                showSeat.setStatus(SeatStatus.BOOKED);
            }
            // Release temp locks once booked permanently
            seatLockProvider.unlockSeats(booking.getShow(), physicalSeats, user);
        } else {
            booking.setStatus(BookingStatus.CANCELLED);
            seatLockProvider.unlockSeats(booking.getShow(), physicalSeats, user);
        }

        return booking;
    }
}
