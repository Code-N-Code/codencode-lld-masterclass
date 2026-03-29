package stackoverflow.model;

import stackoverflow.enums.VoteType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Answer extends Post {
    private final String questionId;
    private boolean isAccepted;
    private final AtomicInteger voteCount;
    // Thread-safe list for concurrent comment additions
    private final List<Comment> comments;

    public Answer(String id, String content, String authorId, String questionId) {
        super(id, content, authorId);
        this.questionId = questionId;
        this.isAccepted = false;
        this.voteCount = new AtomicInteger(0);
        this.comments = new CopyOnWriteArrayList<>();
    }

    public void vote(VoteType voteType) {
        voteCount.addAndGet(voteType.getValue());
    }

    public void addComment(Comment comment) { this.comments.add(comment); }
    public int getVoteCount() { return voteCount.get(); }
    public boolean isAccepted() { return isAccepted; }
    public void accept() { this.isAccepted = true; }


    @Override
    public String toString() {
        return  "Answer: " + content + "\n" +
                "Author: " + authorId + "\n" +
                "Vote: " + voteCount + "\n";
    }
}
