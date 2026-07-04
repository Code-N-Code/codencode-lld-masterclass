package hotelmanagement.service;

import hotelmanagement.exception.HotelException;
import hotelmanagement.model.DateRange;
import hotelmanagement.model.RoomType;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryInventoryManager implements InventoryManager {

    /*
     * Data structure:
     *
     * reservedRooms:
     *
     * roomTypeId -> date -> reserved count
     *
     * Example:
     *
     * DELUXE -> 2026-07-01 -> 3
     * DELUXE -> 2026-07-02 -> 2
     * DELUXE -> 2026-07-03 -> 4
     */
    private final Map<String, Map<LocalDate, Integer>> reservedRooms =
            new ConcurrentHashMap<>();

    @Override
    public synchronized int getAvailableRooms(
            RoomType roomType,
            DateRange dateRange
    ) {
        int minimumAvailable = Integer.MAX_VALUE;

        Map<LocalDate, Integer> reservedByDate =
                reservedRooms.getOrDefault(
                        roomType.id(),
                        Collections.emptyMap()
                );

        for (LocalDate night : dateRange.getNights()) {
            int alreadyReserved = reservedByDate.getOrDefault(night, 0);
            int available = roomType.totalRooms() - alreadyReserved;

            minimumAvailable = Math.min(minimumAvailable, available);
        }

        return minimumAvailable;
    }

    @Override
    public synchronized void reserve(
            RoomType roomType,
            DateRange dateRange,
            int quantity
    ) {
        if (quantity <= 0) {
            throw new HotelException("Quantity must be positive");
        }

        int availableRooms = getAvailableRooms(roomType, dateRange);

        if (availableRooms < quantity) {
            throw new HotelException("Rooms are not available");
        }

        Map<LocalDate, Integer> reservedByDate =
                reservedRooms.computeIfAbsent(
                        roomType.id(),
                        key -> new ConcurrentHashMap<>()
                );

        for (LocalDate night : dateRange.getNights()) {
            reservedByDate.merge(night, quantity, Integer::sum);
        }
    }

    @Override
    public synchronized void release(
            RoomType roomType,
            DateRange dateRange,
            int quantity
    ) {
        if (quantity <= 0) {
            throw new HotelException("Quantity must be positive");
        }

        Map<LocalDate, Integer> reservedByDate =
                reservedRooms.get(roomType.id());

        if (reservedByDate == null) {
            throw new HotelException("No reserved inventory found for room type");
        }

        for (LocalDate night : dateRange.getNights()) {
            int currentReserved = reservedByDate.getOrDefault(night, 0);

            if (currentReserved < quantity) {
                throw new HotelException("Cannot release more rooms than reserved");
            }

            int updatedReserved = currentReserved - quantity;

            if (updatedReserved == 0) {
                reservedByDate.remove(night);
            } else {
                reservedByDate.put(night, updatedReserved);
            }
        }
    }
}