package stackoverflow.service;

import stackoverflow.dto.Filter;
import stackoverflow.enums.VoteType;
import stackoverflow.model.Answer;
import stackoverflow.model.Question;
import stackoverflow.model.Tag;
import stackoverflow.model.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class StackOverflowService {
    // Thread-safe collections for in-memory DBhai
    private final UserService userService;
    private final QuestionService questionService;

    public StackOverflowService(UserService userService,  QuestionService questionService) {
        this.userService = userService;
        this.questionService = questionService;
    }

    public User createUser(String username) {
        User user = new User(UUID.randomUUID().toString(), username);
        userService.save(user);
        return user;
    }

    public Question askQuestion(String authorId, String title, String content, Set<Tag> tags) {
        // Validating valid author
        userService.findUserOrThrow(authorId);
        Question question = new Question(UUID.randomUUID().toString(), title, content, authorId, tags);
        questionService.save(question);
        return question;
    }

    public Answer answerQuestion(String authorId, String questionId, String content) {
        // Validating valid author
        userService.findUserOrThrow(authorId);

        // Validating and fetching question
        Question question = questionService.findQuestionOrThrow(questionId);

        Answer answer = new Answer(UUID.randomUUID().toString(), content, authorId, questionId);
        question.addAnswer(answer);
        return answer;
    }

    // Example of Search logic
    public List<Question> searchQuestion(Filter filter) {
        return questionService.findAll(filter);
    }

    // Vote question
    public void voteQuestion(String questionId, VoteType voteType) {
        // Validation
        Question question = questionService.findQuestionOrThrow(questionId);
        User author = userService.findUserOrThrow(question.getAuthorId());

        int reputationDelta = (voteType == VoteType.UPVOTE) ? 5 : -2;
        userService.updateReputation(author.getId(), reputationDelta);
        questionService.voteQuestion(questionId, voteType);
    }

    // Vote answer
    public void voteAnswer(String questionId, String answerId, VoteType voteType) {
        // Validation
        Answer answer = questionService.findAnswerOrThrow(questionId, answerId);
        User author = userService.findUserOrThrow(answer.getAuthorId());
        int reputationDelta = (voteType == VoteType.UPVOTE) ? 10 : -2;

        userService.updateReputation(author.getId(), reputationDelta);
        questionService.voteAnswer(questionId, answerId, voteType);
    }
}
