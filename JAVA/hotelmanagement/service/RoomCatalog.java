package hotelmanagement.service;

import com.hotel.lld.exception.HotelException;
import com.hotel.lld.model.RoomType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomCatalog {

    private final Map<String, RoomType> roomTypes = new ConcurrentHashMap<>();

    public void addRoomType(RoomType roomType) {
        roomTypes.put(roomType.id(), roomType);
    }

    public RoomType getRoomType(String roomTypeId) {
        RoomType roomType = roomTypes.get(roomTypeId);

        if (roomType == null) {
            throw new HotelException("Room type not found: " + roomTypeId);
        }

        return roomType;
    }
}