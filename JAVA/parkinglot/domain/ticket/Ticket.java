package parkinglot.domain.ticket;

import parkinglot.domain.parking.ParkingSpot;
import parkinglot.domain.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private final String ticketId;
    private final ParkingSpot spot;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;

    private LocalDateTime exitTime;
    private Double charges;

    public Ticket(ParkingSpot spot, Vehicle vehicle) {
        this.ticketId = UUID.randomUUID().toString();
        this.spot = spot;
        this.vehicle = vehicle;
        this.entryTime = LocalDateTime.now();
    }

    public void closeTicket(double charges) {
        this.charges = charges;
    }

    // Setters
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    // Getters.
    public String getTicketId() {
        return ticketId;
    }
    public ParkingSpot getSpot() {
        return spot;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    public LocalDateTime getExitTime() {
        return exitTime;
    }
    public Double getCharges() {
        return charges;
    }
}
