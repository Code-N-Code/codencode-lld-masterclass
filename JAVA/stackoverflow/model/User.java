package stackoverflow.model;

import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private final String id;
    private final String username;
    // Thread-safe integer for concurrent reputation updates
    private final AtomicInteger reputation;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
        this.reputation = new AtomicInteger(0);
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public int getReputation() { return reputation.get(); }

    public void updateReputation(int delta) {
        reputation.addAndGet(delta);
    }

    @Override
    public String toString() {
        return "User: " + username + "\n" +
               "id: " + id + "\n" +
               "reputation: " +  reputation;
    }
}
