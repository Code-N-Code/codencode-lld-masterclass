package bookmyshow.models;

import bookmyshow.enums.BookingStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private String id;
    private User user;
    private Show show;
    private List<ShowSeat> seats;
    private double totalAmount;
    private BookingStatus status;
    private Instant createdAt;

    public Booking() {
        this.seats = new ArrayList<>();
    }

    public Booking(String id, User user, Show show, List<ShowSeat> seats, double totalAmount, BookingStatus status, Instant createdAt) {
        this.id = id;
        this.user = user;
        this.show = show;
        this.seats = seats != null ? seats : new ArrayList<>();
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public List<ShowSeat> getSeats() {
        return seats;
    }

    public void setSeats(List<ShowSeat> seats) {
        this.seats = seats;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
