package hotelmanagement.service;

import hotelmanagement.exception.HotelException;
import hotelmanagement.model.DateRange;
import hotelmanagement.model.RoomType;

import java.math.BigDecimal;

public class BasicPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(
            RoomType roomType,
            DateRange dateRange,
            int quantity
    ) {
        if (quantity <= 0) {
            throw new HotelException("Quantity must be positive");
        }

        return roomType.basePrice()
                .multiply(BigDecimal.valueOf(dateRange.numberOfNights()))
                .multiply(BigDecimal.valueOf(quantity));
    }
}