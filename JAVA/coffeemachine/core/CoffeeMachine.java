package coffeemachine.core;

import coffeemachine.models.Beverage;
import coffeemachine.models.Ingredient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CoffeeMachine {
    private static volatile CoffeeMachine instance;
    private final Inventory inventory;
    private final Map<String, Beverage> menu;

    private CoffeeMachine() {
        this.inventory = new Inventory();
        this.menu = new ConcurrentHashMap<>();
    }

    public static CoffeeMachine getInstance() {
        if (instance == null) {
            synchronized (CoffeeMachine.class) {
                if (instance == null) {
                    instance = new CoffeeMachine();
                }
            }
        }
        return instance;
    }

    public void addMenuItem(Beverage beverage) {
        menu.put(beverage.getName().toUpperCase(), beverage);
    }

    public void refill(Ingredient ingredient, int quantity) {
        inventory.addIngredients(ingredient, quantity);
    }

    public void orderBeverage(String name) {
        Beverage beverage = menu.get(name.toUpperCase());
        if (beverage == null) {
            System.out.println("Error: " + name + " is not on the menu.");
            return;
        }

        try {
            inventory.checkAndConsume(beverage.getRecipe());
            System.out.println("[SUCCESS] Dispensing " + beverage.getName() + " ($" + beverage.getPrice() + ")");
        } catch (Exception e) {
            System.out.println("[FAILED] " + beverage.getName() + ": " + e.getMessage());
        }
    }

    public void showStatus() {
        inventory.displayInventory();
    }
}
