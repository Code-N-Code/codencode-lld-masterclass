package bookmyshow.strategies;

import bookmyshow.models.ShowSeat;

import java.util.List;

public class DefaultPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(List<ShowSeat> seats) {
        double total = 0.0;
        for (ShowSeat seat : seats) {
            total += seat.getPrice();
        }
        return total;
    }
}
