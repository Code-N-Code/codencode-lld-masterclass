package vendingmachine;

import vendingmachine.inventory.Inventory;
import vendingmachine.model.Coin;
import vendingmachine.model.Item;
import vendingmachine.state.IdleState;
import vendingmachine.state.VendingMachineState;

public class VendingMachine {
    private VendingMachineState currentState;

    // ── Data ─────────────────────────────────────────────────────────────────
    private final Inventory inventory;
    private int balance;          // accumulated coins in cents
    private Item selectedItem;

    public  VendingMachine() {
        this.currentState = IdleState.getInstance();
        this.inventory = new Inventory();
        this.balance = 0;
    }

    // ── Public API (delegated to current state) ───────────────────────────────
    public void insertCoin(Coin coin) {
        currentState.insertCoin(this, coin);
    }
    public void selectItem(Item item) {
        currentState.selectItem(this, item);
    }
    public void dispense() {
        currentState.dispense(this);
    }
    public void cancel() {
        currentState.cancel(this);
    }


    // ── State Management ───────────────────────────────
    public void setState(VendingMachineState state) {
        this.currentState = state;
    }

    // ── Balance Management ───────────────────────────────
    public void addBalance(int amount) {
        this.balance += amount;
    }

    public int getBalance() {
        return this.balance;
    }

    public void resetBalance() {
        this.balance = 0;
    }

    // ── Item helpers ──────────────────────────────────────────────────────────
    public void setSelectedItem(Item item) {
        this.selectedItem = item;
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    // ── Inventory ─────────────────────────────────────────────────────────────
    public Inventory getInventory() {
        return inventory;
    }
}
