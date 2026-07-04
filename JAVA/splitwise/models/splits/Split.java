package splitwise.models.splits;

import splitwise.models.User;

public abstract class Split {
    protected User user;
    protected double amount;

    public Split(User user) {
        this.user = user;
    }

    public User getUser() { return user; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
