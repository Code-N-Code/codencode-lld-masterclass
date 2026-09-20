package amazon.service;

import amazon.model.Product;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryService {
    // ConcurrentHashMap handles thread-safe reads and additions to the catalog
    private final Map<String, Product> inventory = new ConcurrentHashMap<>();

    public void addProduct(Product product) {
        inventory.put(product.getId(), product);
    }

    public Product getProduct(String productId) {
        return inventory.get(productId);
    }

    /**
     * THREAD-SAFE STOCK DEDUCTION
     * We synchronize on the specific product instance rather than the whole method.
     * This ensures Users buying Product A don't block Users buying Product B.
     */
    public boolean deductStock(String productId, int quantityRequested) {
        Product product = inventory.get(productId);
        if (product == null) return false;

        synchronized (product) {
            if (product.getStockQuantity() >= quantityRequested) {
                product.setStockQuantity(product.getStockQuantity() - quantityRequested);
                return true;
            }
            return false; // Out of stock or insufficient quantity
        }
    }

    public void restoreStock(String productId, int quantityToRestore) {
        Product product = inventory.get(productId);
        if (product != null) {
            synchronized (product) {
                product.setStockQuantity(product.getStockQuantity() + quantityToRestore);
            }
        }
    }
}
