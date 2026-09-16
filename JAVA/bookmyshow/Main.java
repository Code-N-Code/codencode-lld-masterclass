package bookmyshow;

import bookmyshow.enums.SeatStatus;
import bookmyshow.enums.SeatType;
import bookmyshow.exceptions.SeatUnavailableException;
import bookmyshow.models.*;
import bookmyshow.providers.InMemorySeatLockProvider;
import bookmyshow.providers.SeatLockProvider;
import bookmyshow.services.BookingService;
import bookmyshow.services.PaymentService;
import bookmyshow.strategies.DefaultPricingStrategy;
import bookmyshow.strategies.PricingStrategy;
import bookmyshow.strategies.WeekendPricingStrategy;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     MOVIE BOOKING SYSTEM - END TO END DEMO       ");
        System.out.println("==================================================\n");

        // ----------------------------------------------------
        // 1. INITIALIZE SYSTEM DATA & DEPENDENCIES
        // ----------------------------------------------------
        User alice = new User("U101", "Alice", "alice@example.com");
        User bob = new User("U102", "Bob", "bob@example.com");

        Movie movie = new Movie("M1", "Inception", 148);

        Seat seat1 = new Seat("S1", 1, 1, SeatType.REGULAR);
        Seat seat2 = new Seat("S2", 1, 2, SeatType.VIP);
        Seat seat3 = new Seat("S3", 1, 3, SeatType.REGULAR);

        Screen screen = new Screen("SCR1", "Screen 1", Arrays.asList(seat1, seat2, seat3));
        Cinema cinema = new Cinema("C1", "PVR IMAX", "Noida", List.of(screen));

        Show show = new Show("SHOW100", movie, screen, System.currentTimeMillis(), System.currentTimeMillis() + 9000000);

        // Map physical seats to show seats
        ShowSeat showSeat1 = new ShowSeat("SS1", show.getId(), seat1, SeatStatus.AVAILABLE, 200.0);
        ShowSeat showSeat2 = new ShowSeat("SS2", show.getId(), seat2, SeatStatus.AVAILABLE, 400.0);
        ShowSeat showSeat3 = new ShowSeat("SS3", show.getId(), seat3, SeatStatus.AVAILABLE, 200.0);

        // System Providers & Services setup
        SeatLockProvider lockProvider = new InMemorySeatLockProvider(300); // 5-minute lock TTL
        PricingStrategy pricingStrategy = new DefaultPricingStrategy();
        PaymentService paymentService = new PaymentService();
        BookingService bookingService = new BookingService(lockProvider, pricingStrategy, paymentService);

        // ----------------------------------------------------
        // 2. SCENARIO 1: ALICE SUCCESSFULLY BOOKS SEATS SS1 & SS2
        // ----------------------------------------------------
        System.out.println("--- SCENARIO 1: Alice creates a booking ---");
        List<ShowSeat> aliceSeats = Arrays.asList(showSeat1, showSeat2);

        Booking aliceBooking = bookingService.createBooking(alice, show, aliceSeats);
        System.out.println("Booking Created for Alice!");
        System.out.println("Booking ID     : " + aliceBooking.getId());
        System.out.println("Total Amount   : $" + aliceBooking.getTotalAmount());
        System.out.println("Booking Status : " + aliceBooking.getStatus());
        System.out.println();

        System.out.println("--- Alice completes payment ---");
        bookingService.confirmPaymentAndBooking(aliceBooking, alice);
        System.out.println("Payment Successful!");
        System.out.println("Booking Status : " + aliceBooking.getStatus());
        System.out.println("Seat 1 Status  : " + showSeat1.getStatus());
        System.out.println("Seat 2 Status  : " + showSeat2.getStatus());
        System.out.println();

        // ----------------------------------------------------
        // 3. SCENARIO 2: BOB TRIES TO BOOK THE ALREADY BOOKED SEAT SS1
        // ----------------------------------------------------
        System.out.println("--- SCENARIO 2: Bob attempts to book already booked seat (SS1) ---");
        try {
            List<ShowSeat> bobSeats = List.of(showSeat1, showSeat3);
            bookingService.createBooking(bob, show, bobSeats);
        } catch (SeatUnavailableException e) {
            System.out.println("Expected Failure: " + e.getMessage());
        }
        System.out.println();

        // ----------------------------------------------------
        // 4. SCENARIO 3: BOB SUCCESSFULLY BOOKS REMAINING SEAT SS3
        // ----------------------------------------------------
        System.out.println("--- SCENARIO 3: Bob books available seat (SS3) ---");
        List<ShowSeat> bobAvailableSeats = List.of(showSeat3);
        Booking bobBooking = bookingService.createBooking(bob, show, bobAvailableSeats);
        System.out.println("Booking Created for Bob!");
        System.out.println("Total Amount   : $" + bobBooking.getTotalAmount());

        bookingService.confirmPaymentAndBooking(bobBooking, bob);
        System.out.println("Bob's Booking Confirmed!");
        System.out.println("Seat 3 Status  : " + showSeat3.getStatus());
        System.out.println();

        // ----------------------------------------------------
        // 5. SCENARIO 4: DEMONSTRATING OPEN/CLOSED PRINCIPLE (SURGE PRICING)
        // ----------------------------------------------------
        System.out.println("--- SCENARIO 4: Applying Weekend Surge Pricing (1.5x) ---");
        PricingStrategy surgePricing = new WeekendPricingStrategy(1.5);
        BookingService surgeBookingService = new BookingService(lockProvider, surgePricing, paymentService);

        // Setup a new seat for weekend calculation demonstration
        Seat seat4 = new Seat("S4", 2, 1, SeatType.VIP);
        ShowSeat showSeat4 = new ShowSeat("SS4", show.getId(), seat4, SeatStatus.AVAILABLE, 400.0);

        Booking surgeBooking = surgeBookingService.createBooking(alice, show, List.of(showSeat4));
        System.out.println("Base Seat Price      : $400.0");
        System.out.println("Calculated Price (1.5x): $" + surgeBooking.getTotalAmount());
    }
}
