package lrucache;

import lrucache.cache.LRUCache;

public class LRUCacheDemo {
    public static void main(String[] args) {
        System.out.println("Demonstrating LRU Cache functionality:");

        // Create an LRU Cache with capacity 3
        LRUCache<Integer, String> cache = new LRUCache<>(3);

        System.out.println("\n--- Adding elements ---");
        cache.put(1, "Apple"); // Cache: {1=Apple}
        cache.put(2, "Banana"); // Cache: {2=Banana, 1=Apple}
        cache.put(3, "Cherry"); // Cache: {3=Cherry, 2=Banana, 1=Apple}
        System.out.println("Cache after adding 3 elements: " + cache);

        System.out.println("\n--- Accessing elements ---");
        System.out.println("Get(2): " + cache.get(2)); // Cache: {2=Banana, 3=Cherry, 1=Apple} (2 becomes MRU)
        System.out.println("Cache after Get(2): " + cache);

        System.out.println("Get(1): " + cache.get(1)); // Cache: {1=Apple, 2=Banana, 3=Cherry} (1 becomes MRU)
        System.out.println("Cache after Get(1): " + cache);

        System.out.println("Get(4) (miss): " + cache.get(4)); // Cache: {1=Apple, 2=Banana, 3=Cherry} (no change)
        System.out.println("Cache after Get(4): " + cache);

        System.out.println("\n--- Adding more elements (triggering eviction) ---");
        cache.put(4, "Date"); // Cache: {4=Date, 1=Apple, 2=Banana} (3 is evicted as it's LRU)
        System.out.println("Cache after Put(4, Date): " + cache);

        cache.put(5, "Elderberry"); // Cache: {5=Elderberry, 4=Date, 1=Apple} (2 is evicted)
        System.out.println("Cache after Put(5, Elderberry): " + cache);

        System.out.println("\n--- Accessing evicted elements ---");
        System.out.println("Get(3) (evicted): " + cache.get(3)); // Should be null
        System.out.println("Cache after Get(3): " + cache);

        System.out.println("\n--- Updating an existing element ---");
        cache.put(4, "Dragonfruit"); // Cache: {4=Dragonfruit, 5=Elderberry, 1=Apple} (4 becomes MRU, value updated)
        System.out.println("Cache after Put(4, Dragonfruit): " + cache);

        System.out.println("Get(4): " + cache.get(4));
        System.out.println("Cache after Get(4): " + cache);
    }
}
