package hotelmanagement.service;

import hotelmanagement.domain.model.DateRange;
import hotelmanagement.domain.model.RoomType;

public interface InventoryManager {

    int getAvailableRooms(RoomType roomType, DateRange dateRange);

    void reserve(RoomType roomType, DateRange dateRange, int quantity);

    void release(RoomType roomType, DateRange dateRange, int quantity);
}