package coffeemachine;

import coffeemachine.core.CoffeeMachine;
import coffeemachine.models.Beverage;
import coffeemachine.models.Ingredient;
import coffeemachine.models.Recipe;

import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        CoffeeMachine machine = CoffeeMachine.getInstance();

        // 1. Setup Ingredients
        machine.refill(Ingredient.WATER, 200);
        machine.refill(Ingredient.MILK, 100);
        machine.refill(Ingredient.COFFEE_BEANS, 50);

        // 2. Setup Menu
        Recipe espressoRecipe = new Recipe(Map.of(Ingredient.WATER, 50, Ingredient.COFFEE_BEANS, 15));
        Beverage espresso = new Beverage("Espresso", 3.50, espressoRecipe);

        Recipe latteRecipe = new Recipe(Map.of(Ingredient.WATER, 50, Ingredient.MILK, 100, Ingredient.COFFEE_BEANS, 15));
        Beverage latte = new Beverage("Latte", 5.00, latteRecipe);

        machine.addMenuItem(espresso);
        machine.addMenuItem(latte);

        machine.showStatus();

        // 3. Simulate Concurrent Access
        // We have only 100 units of Milk. If two people order Latte, one must fail.
        Thread t1 = new Thread(() -> machine.orderBeverage("Latte"), "User-1");
        Thread t2 = new Thread(() -> machine.orderBeverage("Latte"), "User-2");
        Thread t3 = new Thread(() -> machine.orderBeverage("Espresso"), "User-3");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join(); t2.join(); t3.join();
        } catch (InterruptedException e) { e.printStackTrace(); }

        machine.showStatus();
    }
}
