package vendingmachine.inventory;

import vendingmachine.model.Item;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {
    private final Map<Item, Integer> stock = new ConcurrentHashMap<>();

    public void addItem(Item item, int quantity) {
        stock.put(item, stock.getOrDefault(item, 0) + quantity);
    }

    public boolean isItemAvailable(Item item) {
        return stock.getOrDefault(item, 0) > 0;
    }

    public void deductItem(Item item) {
        if(!isItemAvailable(item)) {
            throw new IllegalArgumentException("Item is out of stock: " + item.getName());
        }

        stock.put(item, stock.get(item) - 1);
    }

    public void display() {
        System.out.println("\n=== Available Items ===");
        stock.forEach((item, qty) ->
                System.out.println("  " + item + "  |  Qty: " + qty));
        System.out.println("=======================");
    }
}
