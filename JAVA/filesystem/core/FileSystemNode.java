package filesystem.core;

public interface FileSystemNode {
    String getName();
    int getSize();
    boolean isDirectory();
    void printTree(String indent);
}
