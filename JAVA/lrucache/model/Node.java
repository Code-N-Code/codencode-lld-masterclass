package lrucache.model;

public class Node<K, V> {
    public K key;
    public V value;
    public Node<K, V> pre, next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
