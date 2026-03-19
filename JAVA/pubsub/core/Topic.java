package pubsub.core;

// ─────────────────────────────────────────────
// 3. Topic.java — Manages one channel
//    Thread-safe: CopyOnWriteArrayList
// ─────────────────────────────────────────────
import pubsub.model.Message;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Topic {
    private final String name;
    private final List<Subscriber> subscribers;

    public Topic(String name) {
        this.name = name;
        this.subscribers = new CopyOnWriteArrayList<>();
    }

    public String getName() { return name; }

    public synchronized void subscribe(Subscriber subscriber) {
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
            System.out.println("  [Topic:" + name + "] Subscribed: " + subscriber);
        }
    }

    public synchronized void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
        System.out.println("  [Topic:" + name + "] Unsubscribed: " + subscriber);
    }

    // Called by broker — iterates the snapshot safely even if
    // a subscriber unsubscribes concurrently
    public void notifySubscribers(Message message) {
        for (Subscriber subscriber : subscribers) {          // safe snapshot iteration
            try {
                subscriber.onMessage(message);
            } catch (Exception e) {
                // One bad subscriber must NOT break delivery to others
                System.err.println("  [Topic:" + name + "] Error notifying subscriber: " + e.getMessage());
            }
        }
    }

    public int subscriberCount() { return subscribers.size(); }
}
