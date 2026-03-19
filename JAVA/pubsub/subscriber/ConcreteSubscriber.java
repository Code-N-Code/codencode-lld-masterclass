package pubsub.subscriber;

import pubsub.core.Subscriber;
import pubsub.model.Message;

// ─────────────────────────────────────────────
// 6. ConcreteSubscriber.java — A real subscriber
// ─────────────────────────────────────────────
public class ConcreteSubscriber implements Subscriber {
    private final String name;

    public ConcreteSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("    >> [" + name + "] received: " + message);
    }

    @Override
    public String toString() { return name; }
}
