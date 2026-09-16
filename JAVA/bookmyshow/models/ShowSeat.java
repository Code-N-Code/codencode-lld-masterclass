package bookmyshow.models;

import bookmyshow.enums.SeatStatus;

public class ShowSeat {
    private String id;
    private String showId;
    private Seat seat;
    private SeatStatus status;
    private double price;

    public ShowSeat() {}

    public ShowSeat(String id, String showId, Seat seat, SeatStatus status, double price) {
        this.id = id;
        this.showId = showId;
        this.seat = seat;
        this.status = status;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
