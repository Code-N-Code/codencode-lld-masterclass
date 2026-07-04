package hotelmanagement.model;

import com.hotel.lld.exception.HotelException;

import java.math.BigDecimal;

public record RoomType(
        String id,
        String name,
        int totalRooms,
        BigDecimal basePrice
) {

    public RoomType {
        if (id == null || id.isBlank()) {
            throw new HotelException("Room type id cannot be empty");
        }

        if (name == null || name.isBlank()) {
            throw new HotelException("Room type name cannot be empty");
        }

        if (totalRooms <= 0) {
            throw new HotelException("Total rooms must be positive");
        }

        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new HotelException("Base price cannot be negative");
        }
    }
}