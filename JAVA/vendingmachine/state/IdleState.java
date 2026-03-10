package vendingmachine.state;

import vendingmachine.VendingMachine;
import vendingmachine.model.Coin;
import vendingmachine.model.Item;

public class IdleState implements VendingMachineState {
    private static final IdleState INSTANCE = new IdleState();

    public static IdleState getInstance() {
        return INSTANCE;
    }
    private IdleState() {}

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addBalance(coin.getValue());
        System.out.println("Inserted coin: " + coin.getValue());
        System.out.println("Current balance is " + machine.getBalance());
        machine.setState(HasCoinState.getInstance());
    }

    @Override
    public void selectItem(VendingMachine machine, Item item) {
        System.out.println("Please insert coins first.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Please insert coins and select an item.");
    }

    @Override
    public void cancel(VendingMachine machine) {
        System.out.println("Nothing to cancel.");
    }
}
