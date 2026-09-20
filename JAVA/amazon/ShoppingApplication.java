package amazon;

import amazon.model.Cart;
import amazon.model.Order;
import amazon.model.Product;
import amazon.model.User;
import amazon.service.CartService;
import amazon.service.InventoryService;
import amazon.service.OrderService;
import amazon.service.PaymentProcessor;
import amazon.strategy.PaymentStrategy;
import amazon.strategy.UPIPayment;

import java.util.Optional;

public class ShoppingApplication {
    public static void main(String[] args) {
        // 1. Initialize Services
        InventoryService inventoryService = new InventoryService();
        CartService cartService = new CartService();
        PaymentProcessor paymentProcessor = new PaymentProcessor();
        OrderService orderService = new OrderService(inventoryService, cartService, paymentProcessor);

        // 2. Setup Data
        Product laptop = new Product("P1", "MacBook Pro", 2000.00, 5);
        Product mouse = new Product("P2", "Logitech Mouse", 50.00, 2);
        inventoryService.addProduct(laptop);
        inventoryService.addProduct(mouse);

        User user1 = new User("U1", "Alice", "alice@example.com");

        // 3. Simulate Shopping
        cartService.addProductToCart(user1.getId(), laptop, 1);
        cartService.addProductToCart(user1.getId(), mouse, 1);
        Optional<Cart> cart = cartService.getCart(user1.getId());
        System.out.println("Cart total: $" + cart.get().calculateTotal());

        // 4. Checkout with a specific Strategy
        PaymentStrategy upi = new UPIPayment("alice@okaxis");

        try {
            Order myOrder = orderService.checkout(user1, upi);
            System.out.println("Inventory left for Mouse: " + inventoryService.getProduct("P2").getStockQuantity());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
