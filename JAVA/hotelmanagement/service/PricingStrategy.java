package hotelmanagement.service;

import com.hotel.lld.model.DateRange;
import com.hotel.lld.model.RoomType;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(
            RoomType roomType,
            DateRange dateRange,
            int quantity
    );
}