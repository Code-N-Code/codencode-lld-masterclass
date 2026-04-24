package filesystem.model;

import filesystem.core.FileSystemNode;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class File implements FileSystemNode {
    private final String name;
    private StringBuilder content;
    private final ReentrantReadWriteLock lock;

    public File(String name) {
        this.name = name;
        this.content = new StringBuilder();
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
            return content.length();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + name + " (" + getSize() + " bytes)");
    }

    public String read() {
        lock.readLock().lock();
        try {
            return content.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(String data, boolean append) {
        lock.writeLock().lock();
        try {
            if(append) {
                content.append(data);
            } else {
                content = new StringBuilder(data);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
