package epam.logic.strategies;

import epam.model.KnowledgeElement;
import java.util.List;

public class SequentialStrategy implements QuestionSelectionStrategy {

    private int currentIndex = 0;

    @Override
    public KnowledgeElement selectQuestion(List<KnowledgeElement> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("Knowledge bank is empty");
        }
        KnowledgeElement selected = elements.get(currentIndex);
        currentIndex = (currentIndex + 1) % elements.size();
        return selected;
    }
}
