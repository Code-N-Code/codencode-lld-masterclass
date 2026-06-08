package elevator.dto;

public class Request {
    private final int requestedFloor;
    private final Direction direction;

    public Request(int requestedFloor, Direction direction) {
        this.requestedFloor = requestedFloor;
        this.direction = direction;
    }

    public int getRequestedFloor() { return requestedFloor; }
    public Direction getDirection() { return direction; }
}
