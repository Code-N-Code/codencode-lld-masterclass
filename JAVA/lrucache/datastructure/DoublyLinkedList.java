package lrucache.datastructure;

import lrucache.model.Node;

public class DoublyLinkedList<K, V> {
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public DoublyLinkedList() {
        head = new Node<>(null, null);
        tail = new Node<>(null, null);
        head.next = tail;
        tail.pre = head;
    }

    public void addAtHead(Node<K, V> node) {
        node.next = head.next;
        node.pre = head;
        head.next.pre = node;
        head.next = node;
    }

    public void removeNode(Node<K, V> node) {
        node.next.pre = node.pre;
        node.pre.next = node.next;
    }

    public void moveToHead(Node<K, V> node) {
        removeNode(node);
        addAtHead(node);
    }

    public Node<K, V> removeFromTail() {
        if (tail.pre == head) return null;

        Node<K, V> removedNode = tail.pre;
        removeNode(tail.pre);
        return removedNode;
    }

    public Node<K, V> getHead() {
        return head;
    }

    public Node<K, V> getTail() {
        return tail;
    }
}
