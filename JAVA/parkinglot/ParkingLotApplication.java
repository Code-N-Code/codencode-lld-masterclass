package parkinglot;

import parkinglot.domain.ticket.Ticket;
import parkinglot.domain.vehicle.Vehicle;
import parkinglot.services.ParkingLotService;

public class ParkingLotApplication {
    private final ParkingLotService service;

    public ParkingLotApplication(ParkingLotService service) {
        this.service = service;
    }

    public Ticket parkVehicle(Vehicle vehicle) {
        return service.parkVehicle(vehicle);
    }

    public double unparkVehicle(Ticket ticket) {
        return service.unparkVehicle(ticket);
    }
}
