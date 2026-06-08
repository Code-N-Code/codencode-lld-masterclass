package elevator.model;

import elevator.dto.Direction;
import elevator.dto.Request;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a single Elevator. Runs on its own thread.
 * Only responsible for its own movement and processing its internal stops.
 */
public class Elevator implements Runnable {
    private final int id;
    private final AtomicInteger currentFloor;
    private final AtomicReference<Direction> currentDirection;

    // ConcurrentSkipListSet keeps floors automatically sorted and is Thread-Safe.
    // O(log n) time cost for basic operations.
    private final ConcurrentSkipListSet<Integer> upRequests;
    private final ConcurrentSkipListSet<Integer> downRequests;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = new AtomicInteger(0); // Ground floor
        this.currentDirection = new AtomicReference<>(Direction.IDLE);
        this.upRequests = new ConcurrentSkipListSet<>();
        // For down requests, we need reverse order (highest floor first)
        this.downRequests = new ConcurrentSkipListSet<>((a, b) -> b.compareTo(a));
    }

    // Called by internal buttons or the Dispatcher
    public void addRequest(Request request) {
        if (request.getDirection() == Direction.UP) {
            upRequests.add(request.getRequestedFloor());
        } else {
            downRequests.add(request.getRequestedFloor());
        }

        // Wake up elevator if it was idle
        if (currentDirection.get() == Direction.IDLE) {
            currentDirection.set(request.getRequestedFloor() > currentFloor.get() ? Direction.UP : Direction.DOWN);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                processRequests();
                Thread.sleep(1000); // Simulate time taken to move floors
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void processRequests() throws InterruptedException {
        if (currentDirection.get() == Direction.UP || currentDirection.get() == Direction.IDLE) {
            processUpRequests();
            processDownRequests();
        } else {
            processDownRequests();
            processUpRequests();
        }

        if (upRequests.isEmpty() && downRequests.isEmpty()) {
            currentDirection.set(Direction.IDLE);
        }
    }

    private void processUpRequests() throws InterruptedException {
        while (!upRequests.isEmpty()) {
            int nextFloor = upRequests.pollFirst(); // Get and remove lowest floor
            moveToFloor(nextFloor);
        }
        if (!downRequests.isEmpty()) {
            currentDirection.set(Direction.DOWN);
        }
    }

    private void processDownRequests() throws InterruptedException {
        while (!downRequests.isEmpty()) {
            int nextFloor = downRequests.pollFirst(); // Get and remove highest floor
            moveToFloor(nextFloor);
        }
        if (!upRequests.isEmpty()) {
            currentDirection.set(Direction.UP);
        }
    }

    private void moveToFloor(int targetFloor) throws InterruptedException {
        System.out.println("Elevator " + id + " moving from " + currentFloor.get() + " to " + targetFloor);
        Thread.sleep(Math.abs(currentFloor.get() - targetFloor) * 500L); // Simulate movement delay
        currentFloor.set(targetFloor);
        System.out.println("Elevator " + id + " opened doors at floor " + targetFloor);
    }

    // Getters for Dispatcher
    public int getId() { return id; }
    public int getCurrentFloor() { return currentFloor.get(); }
    public Direction getCurrentDirection() { return currentDirection.get(); }
}
