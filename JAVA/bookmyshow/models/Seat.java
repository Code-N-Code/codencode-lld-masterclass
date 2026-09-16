package bookmyshow.models;

import bookmyshow.enums.SeatType;

public class Seat {
    private String id;
    private int rowNo;
    private int seatNo;
    private SeatType seatType;

    public Seat() {}

    public Seat(String id, int rowNo, int seatNo, SeatType seatType) {
        this.id = id;
        this.rowNo = rowNo;
        this.seatNo = seatNo;
        this.seatType = seatType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }
}
