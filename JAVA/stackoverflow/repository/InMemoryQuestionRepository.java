package stackoverflow.repository;

import stackoverflow.model.Question;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryQuestionRepository implements QuestionRepository {
    private final Map<String, Question> questions = new ConcurrentHashMap<>();

    @Override
    public void save(Question question) {
        questions.put(question.getId(), question);
    }

    @Override
    public Optional<Question> findByQuestionId(String questionId) {
        return Optional.ofNullable(questions.get(questionId));
    }

    @Override
    public List<Question> findAll() {
        return List.copyOf(questions.values());
    }
}
