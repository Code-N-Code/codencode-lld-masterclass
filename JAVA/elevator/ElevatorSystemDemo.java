package elevator;

import elevator.controller.ElevatorController;
import elevator.dto.Direction;
import elevator.strategy.NearestElevatorStrategy;

public class ElevatorSystemDemo {
    public static void main(String[] args) throws InterruptedException {
        // Initialize system with 3 elevators and the Nearest Elevator Strategy
        ElevatorController controller = new ElevatorController(3, new NearestElevatorStrategy());

        System.out.println("--- Elevator System Booted ---");

        // Simulate Hallway requests
        controller.submitExternalRequest(5, Direction.UP);
        controller.submitExternalRequest(2, Direction.DOWN);

        Thread.sleep(1000); // Give elevators time to move

        // Simulate someone getting into Elevator 1 and pressing floor 10
        controller.submitInternalRequest(1, 10);

        // Wait to allow threads to process
        Thread.sleep(10000);
    }
}
