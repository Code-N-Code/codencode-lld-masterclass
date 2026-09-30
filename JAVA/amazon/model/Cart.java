package amazon.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final Map<String, CartItem> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    public synchronized void addItem(Product product, int quantity) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("Product and Product ID must not be null");
        }
        CartItem existingItem = items.get(product.getId());
        if (existingItem == null) {
            items.put(product.getId(), new CartItem(product, quantity));
        } else {
            existingItem.addQuantity(quantity);
        }
    }

    public synchronized void clear() { items.clear(); }

    public synchronized Map<String, CartItem> getItems() { return Collections.unmodifiableMap(items); }

    public synchronized double calculateTotal() {
        return items.values().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}
