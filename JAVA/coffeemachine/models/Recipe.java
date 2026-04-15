package coffeemachine.models;

import java.util.Collections;
import java.util.Map;

public class Recipe {
    private final Map<Ingredient, Integer> composition;

    public Recipe(Map<Ingredient, Integer> composition) {
        // Unmodifiable map to ensure recipe integrity
        this.composition = Collections.unmodifiableMap(composition);
    }

    public Map<Ingredient, Integer> getComposition() {
        return composition;
    }
}
