package epam.logic.strategies;

import epam.model.KnowledgeElement;
import java.util.List;

public class FixedStrategy implements QuestionSelectionStrategy {

    private final int questionIndex;

    public FixedStrategy(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    @Override
    public KnowledgeElement selectQuestion(List<KnowledgeElement> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("Knowledge bank is empty");
        }
        if (questionIndex < 0 || questionIndex >= elements.size()) {
            throw new IllegalArgumentException("Question index out of bounds: " + questionIndex);
        }
        return elements.get(questionIndex);
    }
}
