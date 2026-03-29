package stackoverflow.model;

import java.time.LocalDateTime;

// Base class utilizing inheritance for shared properties
public abstract class Post {
    protected final String id;
    protected String content;
    protected final String authorId;
    protected final LocalDateTime creationDate;

    public Post(String id, String content, String authorId) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.creationDate = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getContent() { return content; }
    public String getAuthorId() { return authorId; }
}
