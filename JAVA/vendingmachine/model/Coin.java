package vendingmachine.model;

public enum Coin {
    ONE(1), FIVE(5), TEN(10), FIFTY(50), HUNDRED(100);

    private final int value;
    Coin(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
