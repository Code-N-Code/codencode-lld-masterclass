package amazon.model;

import amazon.exception.InvalidQuantityException;

public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    public synchronized void  addQuantity(int amount) { this.quantity += amount; }
}
