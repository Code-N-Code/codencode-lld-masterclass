package pubsub.core;

import pubsub.model.Message;

// ─────────────────────────────────────────────
// 2. Subscriber.java — The contract
// ─────────────────────────────────────────────
public interface Subscriber {
    void onMessage(Message message);
}
