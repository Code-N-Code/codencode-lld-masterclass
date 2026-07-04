package hotelmanagement;

import com.hotel.lld.model.Booking;
import com.hotel.lld.model.DateRange;
import com.hotel.lld.model.RoomType;
import com.hotel.lld.repository.BookingRepository;
import com.hotel.lld.repository.InMemoryBookingRepository;
import com.hotel.lld.service.BasicPricingStrategy;
import com.hotel.lld.service.BookingService;
import com.hotel.lld.service.CancellationPolicy;
import com.hotel.lld.service.FreeCancellationPolicy;
import com.hotel.lld.service.InMemoryInventoryManager;
import com.hotel.lld.service.InventoryManager;
import com.hotel.lld.service.PricingStrategy;
import com.hotel.lld.service.RoomCatalog;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HotelManagementApp {

    public static void main(String[] args) {
        RoomCatalog roomCatalog = new RoomCatalog();

        RoomType deluxeRoom = new RoomType(
                "DELUXE",
                "Deluxe Room",
                2,
                new BigDecimal("5000")
        );

        roomCatalog.addRoomType(deluxeRoom);

        InventoryManager inventoryManager = new InMemoryInventoryManager();
        PricingStrategy pricingStrategy = new BasicPricingStrategy();
        CancellationPolicy cancellationPolicy = new FreeCancellationPolicy();
        BookingRepository bookingRepository = new InMemoryBookingRepository();

        BookingService bookingService = new BookingService(
                roomCatalog,
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
        System.out.println(bookingService.searchAvailability("DELUXE", stayDates));

        Booking booking = bookingService.book(
                "Rahul",
                "DELUXE",
                1,
                stayDates
        );

        System.out.println("Created booking:");
        System.out.println(booking);

        System.out.println("Available after booking:");
        System.out.println(bookingService.searchAvailability("DELUXE", stayDates));

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