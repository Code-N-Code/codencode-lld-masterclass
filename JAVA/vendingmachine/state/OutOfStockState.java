package vendingmachine.state;

import vendingmachine.VendingMachine;
import vendingmachine.model.Coin;
import vendingmachine.model.Item;

public class OutOfStockState implements VendingMachineState {
    private static final OutOfStockState INSTANCE = new OutOfStockState();
    public static OutOfStockState getInstance() {
        return INSTANCE;
    }
    private OutOfStockState() {}

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        System.out.println("Machine is out of stock. Returning coin.");
    }

    @Override
    public void selectItem(VendingMachine machine, Item item) {
        System.out.println("Machine is out of stock.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Cannot dispense. Machine is out of stock.");
    }

    @Override
    public void cancel(VendingMachine machine) {
        System.out.println("Cannot dispense. Machine is out of stock.");
    }
}
