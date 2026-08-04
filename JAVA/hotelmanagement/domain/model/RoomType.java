package hotelmanagement.domain.model;

import java.math.BigDecimal;

public enum RoomType {
    STANDARD(10, new BigDecimal("3000")),
    DELUXE(2, new BigDecimal("5000")),
    SUITE(2, new BigDecimal("9000"));

    private final int totalRooms;
    private final BigDecimal basePrice;

    RoomType(int totalRooms, BigDecimal basePrice) {
        this.totalRooms = totalRooms;
        this.basePrice = basePrice;
    }

    public int totalRooms() {
        return totalRooms;
    }

    public BigDecimal basePrice() {
        return basePrice;
    }
}
