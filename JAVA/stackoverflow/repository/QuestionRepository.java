package stackoverflow.repository;

import stackoverflow.model.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {
    void save(Question question);
    Optional<Question> findByQuestionId(String questionId);
    List<Question> findAll();
}
