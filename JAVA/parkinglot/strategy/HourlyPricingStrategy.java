package parkinglot.strategy;

import parkinglot.domain.ticket.Ticket;

import java.time.Duration;

public class HourlyPricingStrategy implements PricingStrategy {
    private final double ratePerHour;

    public HourlyPricingStrategy(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    @Override
    public Double calculatePrice(Ticket ticket) {
        long hours = Duration.between(
                ticket.getEntryTime(),
                ticket.getExitTime()
        ).toHours();

        return Math.max(hours, 1) * ratePerHour;
    }
}
