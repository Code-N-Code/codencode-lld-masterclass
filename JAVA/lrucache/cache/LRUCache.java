package lrucache.cache;

import lrucache.datastructure.DoublyLinkedList;
import lrucache.model.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class LRUCache<K, V> {
    private final int capacity;
    private final DoublyLinkedList<K, V> nodeList;
    private final Map<K, Node<K, V>> nodeMap;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        nodeList = new DoublyLinkedList<>();
        nodeMap = new HashMap<>();
    }

    public synchronized V get(K key) {
        if (!nodeMap.containsKey(key)) return null;

        Node<K, V> node = nodeMap.get(key);
        nodeList.moveToHead(node);
        return node.value;
    }

    public synchronized void put(K key, V value) {
        if (nodeMap.containsKey(key)) {
            Node<K, V> node = nodeMap.get(key);
            node.value = value;
            nodeList.moveToHead(node);
        } else {
            if (nodeMap.size() >= capacity) {
                Node<K, V> evicted = nodeList.removeFromTail();
                if (evicted != null) nodeMap.remove(evicted.key);
            }
            Node<K, V> newNode = new Node<>(key, value);
            nodeList.addAtHead(newNode);
            nodeMap.put(key, newNode);
        }
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        Node<K, V> current = nodeList.getHead().next; // Start after sentinel head
        while (current != nodeList.getTail()) { // Stop at sentinel tail
            joiner.add(current.key + "=" + current.value);
            current = current.next;
        }
        return joiner.toString();
    }
}
