package elevator.strategy;

import elevator.dto.Request;
import elevator.model.Elevator;

import java.util.List;

/**
 * Interface for Dispatch logic. Open for extension.
 */
public interface DispatchStrategy {
    Elevator selectOptimalElevator(List<Elevator> elevators, Request request);
}
