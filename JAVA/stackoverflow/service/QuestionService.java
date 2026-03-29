package stackoverflow.service;

import stackoverflow.dto.Filter;
import stackoverflow.enums.VoteType;
import stackoverflow.model.Answer;
import stackoverflow.model.Question;
import stackoverflow.repository.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionService {
    private final QuestionRepository questionRepository;
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void save(Question question) {
        questionRepository.save(question);
    }

    public List<Question> findAll(Filter filter) {
        return questionRepository.findAll().stream()
                .filter(q -> {
                    // 1. Tag Filter: If tag is null, true. Otherwise, check if question contains tag.
                    boolean matchesTag = (filter.getTag() == null) ||
                            q.getTags().contains(filter.getTag());

                    // 2. User Filter: If userId is null, true. Otherwise, check author ID.
                    boolean matchesUser = (filter.getAuthorId() == null) ||
                            q.getAuthorId().equals(filter.getAuthorId());

                    // Only return true if BOTH conditions are satisfied
                    return matchesTag && matchesUser;
                })
                .collect(Collectors.toList());
    }

    public void voteQuestion(String questionId, VoteType voteType) {
        Question question = findQuestionOrThrow(questionId);
        question.vote(voteType);
    }

    public void voteAnswer(String questionId, String answerId, VoteType voteType) {
        Answer answer = findAnswerOrThrow(questionId, answerId);
        answer.vote(voteType);
    }

    public Answer findAnswerOrThrow(String questionId, String answerId) {
        Question question = findQuestionOrThrow(questionId);
        return question.getAnswers().stream().filter(answer -> answer.getId().equals(answerId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Answer not found: " + answerId));
    }

    public Question findQuestionOrThrow(String questionId) {
        return questionRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + questionId));
    }
}
