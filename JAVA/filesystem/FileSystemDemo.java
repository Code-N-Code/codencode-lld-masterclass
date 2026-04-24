package filesystem;

import filesystem.service.FileSystemService;

public class FileSystemDemo {
    static void main() {
        FileSystemService fs = new FileSystemService();

        // Setup structure
        fs.createDirectory("/", "home");
        fs.createDirectory("/home", "user");
        fs.createFile("/home/user", "notes.txt");
        fs.writeToFile("/home/user", "notes.txt", "Interview prep", false);

        fs.createDirectory("/home/user", "projects");
        fs.createFile("/home/user/projects", "App.java");
        fs.writeToFile("/home/user/projects", "App.java", "public class App {}", false);

        // Print the tree
        fs.printDirectoryTree("/");

        String content = fs.readFromFile("/home/user", "notes.txt");
        System.out.println(content);
    }
}
