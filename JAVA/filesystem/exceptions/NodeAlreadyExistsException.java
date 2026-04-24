package filesystem.exceptions;

public class NodeAlreadyExistsException extends RuntimeException {
    public NodeAlreadyExistsException(String message) {
        super(message);
    }
}
