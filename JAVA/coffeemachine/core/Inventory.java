package coffeemachine.core;

import coffeemachine.exceptions.InsufficientIngredientException;
import coffeemachine.models.Ingredient;
import coffeemachine.models.Recipe;

import java.util.EnumMap;
import java.util.Map;

public class Inventory {
    private final Map<Ingredient, Integer> stock = new EnumMap<>(Ingredient.class);

    public Inventory() {
        for(Ingredient ingredient : Ingredient.values()) {
            stock.put(ingredient, 0);
        }
    }

    public synchronized void addIngredients(Ingredient ingredient, int quantity) {
        stock.put(ingredient, stock.get(ingredient) + quantity);
    }

    /**
     * THREAD SAFETY:
     * This method is synchronized to ensure the 'Check-and-Deduct'
     * routine is atomic. No two threads can pass the check and
     * deduct ingredients simultaneously.
     */
    public synchronized void checkAndConsume(Recipe recipe) throws InsufficientIngredientException {
        // 1. Check Availability
        for (Map.Entry<Ingredient, Integer> entry : recipe.getComposition().entrySet()) {
            int available = stock.get(entry.getKey());
            if (available < entry.getValue()) {
                throw new InsufficientIngredientException("Insufficient " + entry.getKey());
            }
        }

        // 2. Deduct (Atomic because of 'synchronized')
        for (Map.Entry<Ingredient, Integer> entry : recipe.getComposition().entrySet()) {
            stock.put(entry.getKey(), stock.get(entry.getKey()) - entry.getValue());
        }
    }

    /**
     * Provides a point-in-time, consistent snapshot of the current ingredient levels.
     * <p>
     * This method is <b>synchronized</b> to ensure a "happens-before" relationship with
     * update operations. It prevents "dirty reads" by waiting for any ongoing
     * transactions (deductions or refills) to complete, ensuring the printed
     * values are accurate and mutually consistent.
     * </p>
     * * @see #checkAndConsume(Recipe)
     * @see #addIngredients(Ingredient, int)
     */
    public synchronized void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        stock.forEach((k, v) -> System.out.println(k + ": " + v + " units"));
        System.out.println("-------------------------\n");
    }
}
