package filesystem.service;

import filesystem.core.FileSystemNode;
import filesystem.exceptions.InvalidOperationException;
import filesystem.exceptions.NodeNotFoundException;
import filesystem.model.Directory;
import filesystem.model.File;

public class FileSystemService {
    private final Directory root;

    public FileSystemService() {
        this.root = new Directory("root");
    }

    // Helper to traverse the tree based on a path string
    private Directory resolveDirectoryPath(String path) {
        if (path == null || !path.startsWith("/")) {
            throw new InvalidOperationException("Invalid path formatting.");
        }

        if (path.equals("/")) {
            return root;
        }

        String[] parts = path.split("/");
        Directory current = root;

        for (int i = 1; i < parts.length; i++) {
            FileSystemNode node = current.getChild(parts[i]);
            if (node == null || !node.isDirectory()) {
                throw new NodeNotFoundException("Invalid directory path: " + parts[i]);
            }
            current = (Directory) node;
        }
        return current;
    }

    public void createFile(String path, String fileName) {
        Directory dir = resolveDirectoryPath(path);
        File newFile = new File(fileName);
        dir.addNode(newFile);
    }

    public void createDirectory(String path, String dirName) {
        Directory dir = resolveDirectoryPath(path);
        Directory newDir = new Directory(dirName);
        dir.addNode(newDir);
    }

    public void writeToFile(String path, String fileName, String content, boolean append) {
        Directory dir = resolveDirectoryPath(path);
        FileSystemNode node = dir.getChild(fileName);

        if (node == null) throw new NodeNotFoundException("File not found");
        if (node.isDirectory()) throw new InvalidOperationException("Cannot write to a directory");

        ((File) node).write(content, append);
    }

    public String readFromFile(String path, String fileName) {
        Directory dir = resolveDirectoryPath(path);
        FileSystemNode node = dir.getChild(fileName);

        if (node == null) throw new NodeNotFoundException("File not found");
        if (node.isDirectory()) throw new InvalidOperationException("Cannot read a directory");

        return ((File) node).read();
    }

    public int getPathSize(String path) {
        if (path.equals("/")) return root.getSize();

        // Basic implementation for dir size.
        // For a specific file, you'd parse the last part of the path differently.
        Directory dir = resolveDirectoryPath(path);
        return dir.getSize();
    }

    // Add this to FileSystemService.java
    public void printDirectoryTree(String path) {
        System.out.println("Tree for path: " + path);
        Directory targetDir = resolveDirectoryPath(path);

        // Start printing with no initial indentation
        targetDir.printTree("");
    }
}
