package amazon.model;

import amazon.exception.InvalidQuantityException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final Map<String, CartItem> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    public void addItem(Product product, int quantity) {

        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("Product and Product ID must not be null");
        }

        // Atomic update per product key
        items.compute(product.getId(), (id, existingItem) -> {
            if (existingItem == null) {
                return new CartItem(product, quantity);
            }
            existingItem.addQuantity(quantity);
            return existingItem;
        });
    }

    public void clear() { items.clear(); }

    public Map<String, CartItem> getItems() { return Collections.unmodifiableMap(items); }

    public double calculateTotal() {
        return items.values().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}
