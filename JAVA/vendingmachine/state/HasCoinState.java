package vendingmachine.state;

import vendingmachine.VendingMachine;
import vendingmachine.model.Coin;
import vendingmachine.model.Item;

public class HasCoinState implements VendingMachineState{
    private static final HasCoinState INSTANCE = new HasCoinState();
    public static HasCoinState getInstance(){
        return INSTANCE;
    }
    private HasCoinState(){}

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addBalance(coin.getValue());
        System.out.println("Inserted coin: " + coin.getValue());
        System.out.println("Current balance is " + machine.getBalance());
    }

    @Override
    public void selectItem(VendingMachine machine, Item item) {
        if(!machine.getInventory().isItemAvailable(item)){
            System.out.println("Sorry, " + item.getName() + " is out of stock.");
            return;
        }

        if(machine.getBalance() < item.getPrice()){
            System.out.println("Insufficient balance, please add " + (item.getPrice() - machine.getBalance()) + " more.");
            return;
        }

        machine.setSelectedItem(item);
        System.out.println("Selected item: " + item);
        machine.setState(ItemSelectedState.getInstance());
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Please select an item first.");
    }

    @Override
    public void cancel(VendingMachine machine) {
        int refund = machine.getBalance();
        machine.resetBalance();
        machine.setState(IdleState.getInstance());
        System.out.println("Cancelled, Please collect refund amount " + refund);
    }
}
