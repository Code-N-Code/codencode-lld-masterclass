package bookmyshow.models;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    private String id;
    private String name;
    private List<Seat> seats;

    public Screen() {
        this.seats = new ArrayList<>();
    }

    public Screen(String id, String name, List<Seat> seats) {
        this.id = id;
        this.name = name;
        this.seats = seats != null ? seats : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
