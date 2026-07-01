package hotelmanagement.service;

import com.hotel.lld.exception.HotelException;
import com.hotel.lld.model.DateRange;
import com.hotel.lld.model.RoomType;

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