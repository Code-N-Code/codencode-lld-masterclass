package hotelmanagement.service;

import hotelmanagement.model.DateRange;
import hotelmanagement.model.RoomType;

public interface InventoryManager {

    int getAvailableRooms(RoomType roomType, DateRange dateRange);

    void reserve(RoomType roomType, DateRange dateRange, int quantity);

    void release(RoomType roomType, DateRange dateRange, int quantity);
}