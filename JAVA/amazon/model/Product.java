package amazon.model;

public class Product {
    private final String id;
    private final String name;
    private final double price;
    private int stockQuantity; // Mutable state, requires synchronization

    public Product(String id, String name, double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }

    // Synchronized to prevent race conditions at the product level
    public synchronized void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
