package elevator.controller;

import elevator.dto.Direction;
import elevator.dto.Request;
import elevator.model.Elevator;
import elevator.strategy.DispatchStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Central system controller. Delegates tasks.
 */
public class ElevatorController {
    private final List<Elevator> elevators;
    private DispatchStrategy dispatchStrategy; // Injectable strategy

    public ElevatorController(int numElevators, DispatchStrategy strategy) {
        this.elevators = new ArrayList<>();
        this.dispatchStrategy = strategy;

        for (int i = 1; i <= numElevators; i++) {
            Elevator elevator = new Elevator(i);
            this.elevators.add(elevator);
            new Thread(elevator).start(); // Start elevator threads
        }
    }

    // Allows dynamic changing of algorithm at runtime (e.g., Rush hour vs Normal hour)
    public void setDispatchStrategy(DispatchStrategy strategy) {
        this.dispatchStrategy = strategy;
    }

    // Called when a user presses a button in the hallway
    public void submitExternalRequest(int floor, Direction direction) {
        Request req = new Request(floor, direction);
        Elevator optimalElevator = dispatchStrategy.selectOptimalElevator(elevators, req);
        System.out.println("Dispatcher assigned Elevator " + optimalElevator.getId() + " to floor " + floor);
        optimalElevator.addRequest(req);
    }

    // Called when a user presses a button inside the elevator
    public void submitInternalRequest(int elevatorId, int targetFloor) {
        Optional<Elevator> optionalElevator = elevators.stream()
                .filter(e -> e.getId() == elevatorId)
                .findFirst();

        if (optionalElevator.isPresent()) {
            Elevator elevator = optionalElevator.get();
            Direction dir = targetFloor > elevator.getCurrentFloor() ? Direction.UP : Direction.DOWN;
            elevator.addRequest(new Request(targetFloor, dir));
        } else {
            System.err.println("Error: Elevator with ID " + elevatorId + " not found.");
        }
    }
}
