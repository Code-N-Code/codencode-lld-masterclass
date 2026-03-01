package parkinglot.domain.parking;

import parkinglot.domain.vehicle.Vehicle;
import parkinglot.domain.vehicle.VehicleType;

public class ParkingSpot {
    private final String id;
    private final VehicleType vehicleType;

    private Vehicle vehicle;

    public ParkingSpot(String id, VehicleType vehicleType) {
        this.id = id;
        this.vehicleType = vehicleType;
    }

    public synchronized boolean parkVehicle(Vehicle vehicle) {
        if(this.vehicle == null && vehicleType == vehicle.getVehicleType()) {
            this.vehicle = vehicle;
            return true;
        }

        return false;
    }

    public synchronized void removeVehicle() {
        this.vehicle = null;
    }

    public boolean isSpotAvailable() {
        return vehicle == null;
    }
    public String getId() {
        return id;
    }
    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
