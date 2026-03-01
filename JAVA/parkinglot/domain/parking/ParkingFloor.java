package parkinglot.domain.parking;

import java.util.List;

public class ParkingFloor {
    private final String floorId;
    private final List<ParkingSpot> parkingSpots;

    public ParkingFloor(String floorId, List<ParkingSpot> parkingSpots) {
        this.floorId = floorId;
        this.parkingSpots = parkingSpots;
    }

    public List<ParkingSpot> getParkingSpots() {
        // Note: don't return the reference, return the copy to make sure no changes can be made.
        return List.copyOf(parkingSpots);
    }

    public String getFloorId() {
        return floorId;
    }
}
