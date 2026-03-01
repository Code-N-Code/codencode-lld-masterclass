package parkinglot.strategy;

import parkinglot.domain.ticket.Ticket;

public interface PricingStrategy {
    Double calculatePrice(Ticket ticket);
}
