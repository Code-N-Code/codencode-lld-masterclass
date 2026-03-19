package pubsub.publisher;

import pubsub.core.MessageBroker;

// ─────────────────────────────────────────────
// 5. Publisher.java — Thin wrapper
// ─────────────────────────────────────────────
public class Publisher {
    private final String name;
    private final MessageBroker broker;

    public Publisher(String name) {
        this.name = name;
        this.broker = MessageBroker.getInstance();
    }

    public void publish(String topicName, String payload) {
        System.out.println("\n[Publisher:" + name + "] Publishing...");
        broker.publish(topicName, payload);
    }
}
