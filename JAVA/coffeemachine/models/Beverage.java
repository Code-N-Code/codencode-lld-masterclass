package coffeemachine.models;

public class Beverage {
    private final String name;
    private final double price;
    private final Recipe recipe;

    public Beverage(String name, double price, Recipe recipe) {
        this.name = name;
        this.price = price;
        this.recipe = recipe;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public Recipe getRecipe() { return recipe; }
}
