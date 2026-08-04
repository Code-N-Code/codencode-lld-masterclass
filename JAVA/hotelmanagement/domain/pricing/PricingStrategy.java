package hotelmanagement.domain.pricing;

import hotelmanagement.domain.model.DateRange;
import hotelmanagement.domain.model.RoomType;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(
            RoomType roomType,
            DateRange dateRange,
            int quantity
    );
}