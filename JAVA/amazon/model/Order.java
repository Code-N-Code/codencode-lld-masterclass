package amazon.model;

import amazon.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public class Order {
    private final String orderId;
    private final User user;
    private final List<CartItem> items;
    private final double totalAmount;
    private OrderStatus status;

    public Order(User user, List<CartItem> items, double totalAmount) {
        this.orderId = UUID.randomUUID().toString();
        this.user = user;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.CREATED;
    }

    public void setStatus(OrderStatus status) { this.status = status; }
    public String getOrderId() { return orderId; }
    public double getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
}
