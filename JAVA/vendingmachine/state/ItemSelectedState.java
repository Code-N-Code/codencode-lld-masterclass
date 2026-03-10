package vendingmachine.state;

import vendingmachine.VendingMachine;
import vendingmachine.model.Coin;
import vendingmachine.model.Item;

public class ItemSelectedState implements VendingMachineState{
    private static final ItemSelectedState INSTANCE = new ItemSelectedState();
    public static ItemSelectedState getInstance() {
        return INSTANCE;
    }
    private ItemSelectedState() {}

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        System.out.println("Item already selected. Dispensing shortly...");
    }

    @Override
    public void selectItem(VendingMachine machine, Item item) {
        System.out.println("Item already selected, please wait...");
    }

    @Override
    public void dispense(VendingMachine machine) {
        Item item = machine.getSelectedItem();
        machine.getInventory().deductItem(item);

        int change = machine.getBalance() - item.getPrice();
        machine.resetBalance();
        machine.setSelectedItem(null);
        System.out.println("Dispensing: " + item.getName() + ".");
        if(change > 0){
            System.out.println("Please collect change amount " + change);
        }
        machine.setState(IdleState.getInstance());
    }

    @Override
    public void cancel(VendingMachine machine) {
        int refund = machine.getBalance();
        machine.resetBalance();
        machine.setSelectedItem(null);
        machine.setState(IdleState.getInstance());
        System.out.println("Cancelled, Please collect refund amount " + refund);
    }
}
