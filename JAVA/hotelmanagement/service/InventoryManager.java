package hotelmanagement.service;

import com.hotel.lld.model.DateRange;
import com.hotel.lld.model.RoomType;

public interface InventoryManager {

    int getAvailableRooms(RoomType roomType, DateRange dateRange);

    void reserve(RoomType roomType, DateRange dateRange, int quantity);

    void release(RoomType roomType, DateRange dateRange, int quantity);
}