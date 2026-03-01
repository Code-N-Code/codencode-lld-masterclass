package parkinglot;

import parkinglot.domain.parking.ParkingFloor;
import parkinglot.domain.parking.ParkingSpot;
import parkinglot.domain.ticket.Ticket;
import parkinglot.domain.vehicle.Bike;
import parkinglot.domain.vehicle.Car;
import parkinglot.domain.vehicle.Vehicle;
import parkinglot.domain.vehicle.VehicleType;
import parkinglot.services.ParkingLotService;
import parkinglot.strategy.HourlyPricingStrategy;
import parkinglot.strategy.PricingStrategy;

import java.util.List;

public class DemoApplication {
    public static void main(String[] args) {
        // parking spot for GF, BIKE
        ParkingSpot spot1 = new ParkingSpot("00-S1", VehicleType.CAR);

        // parking spot for 1st Floor, CAR
        ParkingSpot spot2 = new ParkingSpot("01-S2", VehicleType.BIKE);

        // Create floors.
        ParkingFloor groundFloor = new ParkingFloor("Ground", List.of(spot1));
        ParkingFloor firstFloor = new ParkingFloor("First", List.of(spot2));

        // Create pricing strategy.
        PricingStrategy strategy = new HourlyPricingStrategy(20);

        ParkingLotService service = new ParkingLotService(List.of(groundFloor, firstFloor), strategy);
        ParkingLotApplication application = new ParkingLotApplication(service);

        // create vehicles to park
        Vehicle bike = new Bike("ABC01");
        Vehicle car = new Car("ABC02");

        // park bike
        Ticket bikeTicket = application.parkVehicle(bike);
        System.out.println("Bike Parked at spot: " + bikeTicket.getSpot().getId());

        // park bike
        Ticket carTicket = application.parkVehicle(car);
        System.out.println("Car Parked at spot: " + carTicket.getSpot().getId());

        // Unparking vehicles.
        System.out.println("Fare: " + application.unparkVehicle(bikeTicket));
        System.out.println("Fare: " + application.unparkVehicle(carTicket));
    }
}
