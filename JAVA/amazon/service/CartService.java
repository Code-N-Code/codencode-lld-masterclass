package amazon.service;

import amazon.model.Cart;
import amazon.model.Product;
import amazon.model.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CartService {
    private final Map<String, Cart> userCarts = new ConcurrentHashMap<>();

    public void addProductToCart(String userId, Product product, int quantity) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        // Atomic thread-safe fetch-or-create followed by thread-safe addItem
        userCarts.computeIfAbsent(userId, k -> new Cart())
                .addItem(product, quantity);
    }

    public Optional<Cart> getCart(String userId) {
        return Optional.ofNullable(userCarts.get(userId));
    }

    public void clearCart(User user) {
        if (userCarts.containsKey(user.getId())) {
            userCarts.get(user.getId()).clear();
        }
    }
}
