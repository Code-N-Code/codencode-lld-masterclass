package amazon.service;

import amazon.enums.OrderStatus;
import amazon.enums.PaymentStatus;
import amazon.model.Cart;
import amazon.model.CartItem;
import amazon.model.Order;
import amazon.model.User;
import amazon.strategy.PaymentStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private final InventoryService inventoryService;
    private final CartService cartService;
    private final PaymentProcessor paymentProcessor;

    // Dependency Injection
    public OrderService(InventoryService inventoryService, CartService cartService, PaymentProcessor paymentProcessor) {
        this.inventoryService = inventoryService;
        this.cartService = cartService;
        this.paymentProcessor = paymentProcessor;
    }

    public Order checkout(User user, PaymentStrategy paymentStrategy) {
        Optional<Cart> cart = cartService.getCart(user.getId());
        if (cart.isEmpty()) {
            throw new RuntimeException("Cart not found");
        }

        if (cart.get().getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        List<CartItem> itemsToOrder = new ArrayList<>(cart.get().getItems().values());
        double total = cart.get().calculateTotal();

        // 1. Deduct Stock (Thread-safe check)
        for (CartItem item : itemsToOrder) {
            boolean success = inventoryService.deductStock(item.getProduct().getId(), item.getQuantity());
            if (!success) {
                // Rollback previously deducted items if one fails
                rollbackStock(itemsToOrder, item.getProduct().getId());
                throw new IllegalStateException("Insufficient stock for: " + item.getProduct().getName());
            }
        }

        // 2. Create Order Object
        Order order = new Order(user, itemsToOrder, total);

        // 3. Process Payment
        PaymentStatus paymentStatus = paymentProcessor.process(paymentStrategy, total);

        if (paymentStatus == PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.PAID);
            cartService.clearCart(user);
            System.out.println("Order successful! ID: " + order.getOrderId());
        } else {
            order.setStatus(OrderStatus.FAILED);
            // Rollback inventory on payment failure
            rollbackStock(itemsToOrder, null);
            System.out.println("Payment failed. Order cancelled.");
        }

        return order;
    }

    private void rollbackStock(List<CartItem> items, String stopAtProductId) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(stopAtProductId)) {
                break;
            }
            inventoryService.restoreStock(item.getProduct().getId(), item.getQuantity());
        }
    }
}
