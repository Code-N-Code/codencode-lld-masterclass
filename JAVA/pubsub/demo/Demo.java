package pubsub.demo;

import pubsub.core.MessageBroker;
import pubsub.core.Subscriber;
import pubsub.publisher.Publisher;
import pubsub.subscriber.ConcreteSubscriber;

public class Demo {
    public static void main(String[] args) throws InterruptedException {

        MessageBroker broker = MessageBroker.getInstance();

        // ── Setup: create topics ──────────────────
        broker.createTopic("sports");
        broker.createTopic("tech");

        // ── Setup: create subscribers ─────────────
        Subscriber alice = new ConcreteSubscriber("Alice");
        Subscriber bob   = new ConcreteSubscriber("Bob");
        Subscriber carol = new ConcreteSubscriber("Carol");

        // ── Subscribe ─────────────────────────────
        broker.subscribe("sports", alice);
        broker.subscribe("sports", bob);
        broker.subscribe("tech",   bob);
        broker.subscribe("tech",   carol);

        // ── Publish ───────────────────────────────
        Publisher espn = new Publisher("ESPN");
        Publisher techCrunch = new Publisher("TechCrunch");

        espn.publish("sports", "Messi scores a hat trick!");
        // Alice and Bob receive this. Carol does not.

        techCrunch.publish("tech", "GPT-5 announced!");
        // Bob and Carol receive this. Alice does not.

        // ── Unsubscribe test ──────────────────────
        System.out.println("\n--- Bob unsubscribes from sports ---");
        broker.unsubscribe("sports", bob);

        espn.publish("sports", "Ronaldo retires.");
        // Now only Alice receives this.

        // ── Thread safety test ────────────────────
        System.out.println("\n--- Concurrency test: 5 threads publishing simultaneously ---");
        Publisher p = new Publisher("StressTest");

        for (int i = 0; i < 5; i++) {
            final int idx = i;
            new Thread(() ->
                    p.publish("sports", "Concurrent message #" + idx)
            ).start();
        }

        Thread.sleep(500); // let threads finish
        System.out.println("\nDone.");
    }
}
