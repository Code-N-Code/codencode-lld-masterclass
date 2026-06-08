package elevator.strategy;

import elevator.dto.Direction;
import elevator.dto.Request;
import elevator.model.Elevator;

import java.util.List;

/**
 * Concrete Strategy: Finds the nearest elevator moving in the same direction or idle.
 */
public class NearestElevatorStrategy implements DispatchStrategy {
    @Override
    public Elevator selectOptimalElevator(List<Elevator> elevators, Request request) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            int distance = Math.abs(elevator.getCurrentFloor() - request.getRequestedFloor());
            Direction eDir = elevator.getCurrentDirection();

            // Check if elevator is idle, or moving towards the request in the same direction
            boolean isMovingTowards = (eDir == Direction.UP && elevator.getCurrentFloor() <= request.getRequestedFloor()) ||
                    (eDir == Direction.DOWN && elevator.getCurrentFloor() >= request.getRequestedFloor());

            if ((eDir == Direction.IDLE || isMovingTowards) && distance < minDistance) {
                minDistance = distance;
                bestElevator = elevator;
            }
        }

        // Fallback: If no ideal elevator found, just pick the first one (in production, we'd queue the request)
        return bestElevator != null ? bestElevator : elevators.getFirst();
    }
}
