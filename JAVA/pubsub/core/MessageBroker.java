package pubsub.core;

// ─────────────────────────────────────────────
// 4. MessageBroker.java — The central registry
//    Singleton + ConcurrentHashMap
// ─────────────────────────────────────────────
import pubsub.model.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageBroker {

    // ── Singleton (thread-safe via class loader) ──
    private static final MessageBroker INSTANCE = new MessageBroker();
    private MessageBroker() {}
    public static MessageBroker getInstance() { return INSTANCE; }

    // ── State ──
    private final Map<String, Topic> topics = new ConcurrentHashMap<>();

    // ── Topic management ──
    public Topic createTopic(String name) {
        // putIfAbsent is atomic — no duplicate topics even under concurrency
        topics.putIfAbsent(name, new Topic(name));
        System.out.println("[Broker] Topic created/fetched: " + name);
        return topics.get(name);
    }

    public Topic getTopic(String name) {
        return topics.get(name);
    }

    // ── Subscribe / Unsubscribe ──
    public void subscribe(String topicName, Subscriber subscriber) {
        Topic topic = getOrThrow(topicName);
        topic.subscribe(subscriber);
    }

    public void unsubscribe(String topicName, Subscriber subscriber) {
        Topic topic = getOrThrow(topicName);
        topic.unsubscribe(subscriber);
    }

    // ── Publish ──
    public void publish(String topicName, String payload) {
        Topic topic = getOrThrow(topicName);
        Message message = new Message(topicName, payload);
        System.out.println("[Broker] Publishing to '" + topicName + "': " + payload);
        topic.notifySubscribers(message);
    }

    private Topic getOrThrow(String name) {
        Topic t = topics.get(name);
        if (t == null) throw new IllegalArgumentException("Topic not found: " + name);
        return t;
    }
}
