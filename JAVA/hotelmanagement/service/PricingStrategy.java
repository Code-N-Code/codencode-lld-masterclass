package hotelmanagement.service;

import hotelmanagement.model.DateRange;
import hotelmanagement.model.RoomType;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(
            RoomType roomType,
            DateRange dateRange,
            int quantity
    );
}