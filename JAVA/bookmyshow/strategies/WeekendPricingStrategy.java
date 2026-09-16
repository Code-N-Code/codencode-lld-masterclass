package bookmyshow.strategies;

import bookmyshow.models.ShowSeat;

import java.util.List;

public class WeekendPricingStrategy implements PricingStrategy {
    private final double surgeMultiplier;

    public WeekendPricingStrategy(double surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    @Override
    public double calculatePrice(List<ShowSeat> seats) {
        double basePrice = 0.0;
        for (ShowSeat seat : seats) {
            basePrice += seat.getPrice();
        }
        return basePrice * surgeMultiplier;
    }
}
