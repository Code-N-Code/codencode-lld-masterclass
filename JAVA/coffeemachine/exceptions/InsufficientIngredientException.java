package coffeemachine.exceptions;

public class InsufficientIngredientException extends Exception {
    public InsufficientIngredientException(String message) {
        super(message);
    }
}
