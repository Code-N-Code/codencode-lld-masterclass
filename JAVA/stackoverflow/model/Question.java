package stackoverflow.model;

import stackoverflow.enums.VoteType;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Question extends Post {
    private final String title;
    private final Set<Tag> tags;
    private final AtomicInteger voteCount;
    private final List<Answer> answers;
    private final List<Comment> comments;

    public Question(String id, String title, String content, String authorId, Set<Tag> tags) {
        super(id, content, authorId);
        this.title = title;
        this.tags = tags;
        this.voteCount = new AtomicInteger(0);
        // Thread-safe lists
        this.answers = new CopyOnWriteArrayList<>();
        this.comments = new CopyOnWriteArrayList<>();
    }

    public void vote(VoteType voteType) {
        voteCount.addAndGet(voteType.getValue());
    }

    public void addAnswer(Answer answer) { this.answers.add(answer); }
    public void addComment(Comment comment) { this.comments.add(comment); }

    public String getTitle() { return title; }
    public int getVoteCount() { return voteCount.get(); }
    public List<Answer> getAnswers() { return answers; }
    public Set<Tag> getTags() { return Set.copyOf(tags); }
}
