package filesystem.model;

import filesystem.core.FileSystemNode;
import filesystem.exceptions.NodeAlreadyExistsException;
import filesystem.exceptions.NodeNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Directory implements FileSystemNode {
    private final String name;
    // Map ensures O(1) lookup.
    private final Map<String, FileSystemNode> children;
    private final ReentrantReadWriteLock lock;

    public Directory(String name) {
        this.name = name;
        this.children = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSize() {
        lock.readLock().lock();
        try {
            int size = 0;
            for(FileSystemNode node : children.values()) {
                size += node.getSize();
            }
            return size;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + name);

        lock.readLock().lock();
        try {
            for (FileSystemNode child : children.values()) {
                // Increase the indentation for children by passing two spaces
                child.printTree(indent + "  ");
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void addNode(FileSystemNode node) {
        lock.writeLock().lock();
        try {
            if (children.containsKey(node.getName())) {
                throw new NodeAlreadyExistsException("Node " + node.getName() + ", already exist.");
            }
            children.put(node.getName(), node);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeNode(String nodeName) {
        lock.writeLock().lock();
        try {
            if(!children.containsKey(nodeName)) {
                throw new NodeNotFoundException("Couldn't find node " + nodeName);
            }
            children.remove(nodeName);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public FileSystemNode getChild(String nodeName) {
        lock.readLock().lock();
        try {
            return children.get(nodeName);
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<FileSystemNode> getChildren() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(children.values());
        } finally {
            lock.readLock().unlock();
        }
    }
}
