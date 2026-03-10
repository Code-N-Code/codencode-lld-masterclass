package vendingmachine;

import vendingmachine.model.Coin;
import vendingmachine.model.Item;
import vendingmachine.state.OutOfStockState;

public class VendingMachineDemo {
    public static void main(String[] args) {
        // ── Setup ────────────────────────────────────────────────────────────
        VendingMachine machine = new VendingMachine();

        Item coke   = new Item("Coke",   50);
        Item chips  = new Item("Chips",  25);
        Item water  = new Item("Water",   20);

        machine.getInventory().addItem(coke,  3);
        machine.getInventory().addItem(chips, 2);
        machine.getInventory().addItem(water, 1);

        machine.getInventory().display();

        // ── Scenario 1: Normal purchase with change ──────────────────────────
        System.out.println("\n--- Scenario 1: Buy Chips (25 Rupees), pay 30 Rupees ---");
        machine.insertCoin(Coin.TEN);   // 10
        machine.insertCoin(Coin.TEN);   // 20
        machine.insertCoin(Coin.TEN);   // 30
        machine.selectItem(chips);
        machine.dispense();             // should return 5 Rupees change

        // ── Scenario 2: Insufficient funds ───────────────────────────────────
        System.out.println("\n--- Scenario 2: Try to buy Coke (50 Rupees) with only 10 Rupees ---");
        machine.insertCoin(Coin.TEN);
        machine.selectItem(coke);           // should say "insufficient balance"
        machine.cancel();                   // refund 50¢

        // ── Scenario 3: Cancel mid-transaction ───────────────────────────────
        System.out.println("\n--- Scenario 3: Cancel after inserting 50 Rupees ---");
        machine.insertCoin(Coin.FIFTY);
        machine.cancel();                   // refund 50 Rupees

        // ── Scenario 4: Action before inserting coin ─────────────────────────
        System.out.println("\n--- Scenario 4: Select item without inserting coin ---");
        machine.selectItem(water);          // should prompt to insert coins first

        // ── Scenario 5: Out of stock ─────────────────────────────────────────
        System.out.println("\n--- Scenario 5: Buy last Water, then try again ---");
        machine.insertCoin(Coin.TEN);
        machine.insertCoin(Coin.TEN);
        machine.selectItem(water);
        machine.dispense();                 // last water dispensed

        // water is now out of stock
        machine.getInventory().addItem(water, 0); // already 0
        machine.setState(OutOfStockState.getInstance());
        machine.insertCoin(Coin.HUNDRED);   // rejected — out of stock

        machine.getInventory().display();
    }
}
