package parkinglot.services;

import parkinglot.domain.parking.ParkingFloor;
import parkinglot.domain.parking.ParkingSpot;
import parkinglot.domain.ticket.Ticket;
import parkinglot.domain.vehicle.Vehicle;
import parkinglot.domain.vehicle.VehicleType;
import parkinglot.exception.ParkingException;
import parkinglot.strategy.PricingStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLotService {
    private final Map<String, Ticket> activeTickets;
    private final List<ParkingFloor>  parkingFloors;
    private PricingStrategy pricingStrategy;

    public ParkingLotService(List<ParkingFloor> parkingFloors, PricingStrategy pricingStrategy) {
        this.parkingFloors = parkingFloors;
        activeTickets = new ConcurrentHashMap<>();
        this.pricingStrategy = pricingStrategy;
    }

    public Ticket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = getAvailableParkingSpot(vehicle.getVehicleType());

        if (!spot.parkVehicle(vehicle)) {
            throw new ParkingException("Parking spot was taken concurrently");
        }
        Ticket ticket = new Ticket(spot, vehicle);
        activeTickets.put(ticket.getTicketId(), ticket);
        return ticket;
    }

    public double unparkVehicle(Ticket ticket) {
        if (!activeTickets.containsKey(ticket.getTicketId())) {
            throw new ParkingException("Invalid ticket: " + ticket.getTicketId());
        }

        ticket.setExitTime(LocalDateTime.now());
        ticket.closeTicket(pricingStrategy.calculatePrice(ticket));
        ticket.getSpot().removeVehicle();
        activeTickets.remove(ticket.getTicketId());
        return ticket.getCharges();
    }

    private ParkingSpot getAvailableParkingSpot(VehicleType vehicleType) {
        // Return any available spot (policy can be improved later)
        for(ParkingFloor parkingFloor : parkingFloors) {
            for (ParkingSpot spot : parkingFloor.getParkingSpots()) {
                if (spot.getVehicleType() == vehicleType && spot.isSpotAvailable()) {
                    return spot;
                }
            }
        }

        throw new ParkingException("No spot available for vehicle type " + vehicleType);
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }
}
