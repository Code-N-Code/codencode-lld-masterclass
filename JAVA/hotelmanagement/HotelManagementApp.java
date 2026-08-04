package hotelmanagement;


import hotelmanagement.domain.model.Booking;
import hotelmanagement.domain.model.DateRange;
import hotelmanagement.domain.model.RoomType;
import hotelmanagement.domain.policy.CancellationPolicy;
import hotelmanagement.domain.policy.FreeCancellationPolicy;
import hotelmanagement.domain.pricing.BasicPricingStrategy;
import hotelmanagement.domain.pricing.PricingStrategy;
import hotelmanagement.repository.BookingRepository;
import hotelmanagement.repository.InMemoryBookingRepository;
import hotelmanagement.service.BookingService;
import hotelmanagement.service.InMemoryInventoryManager;
import hotelmanagement.service.InventoryManager;

import java.time.LocalDate;

public class HotelManagementApp {

     static void main() {
        InventoryManager inventoryManager = new InMemoryInventoryManager();
        PricingStrategy pricingStrategy = new BasicPricingStrategy();
        CancellationPolicy cancellationPolicy = new FreeCancellationPolicy();
        BookingRepository bookingRepository = new InMemoryBookingRepository();

        BookingService bookingService = new BookingService(
                inventoryManager,
                pricingStrategy,
                cancellationPolicy,
                bookingRepository
        );

        DateRange stayDates = new DateRange(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 4)
        );

        System.out.println("Available before booking:");
        System.out.println(bookingService.searchAvailability(RoomType.DELUXE, stayDates));

        Booking booking = bookingService.book(
                "Rahul",
                RoomType.DELUXE,
                1,
                stayDates
        );

        System.out.println("Created booking:");
        System.out.println(booking);

        System.out.println("Available after booking:");
        System.out.println(bookingService.searchAvailability(RoomType.DELUXE, stayDates));

        bookingService.checkIn(
                booking.getId(),
                LocalDate.of(2026, 7, 1)
        );

        System.out.println("Booking after check-in:");
        System.out.println(bookingService.getBooking(booking.getId()));

        bookingService.checkOut(booking.getId());

        System.out.println("Booking after checkout:");
        System.out.println(bookingService.getBooking(booking.getId()));
    }
}
