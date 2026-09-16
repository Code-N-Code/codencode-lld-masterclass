package bookmyshow.strategies;

import bookmyshow.models.ShowSeat;

import java.util.List;

public interface PricingStrategy {
    double calculatePrice(List<ShowSeat> seats);
}
