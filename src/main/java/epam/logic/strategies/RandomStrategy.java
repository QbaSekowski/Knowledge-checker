package epam.logic.strategies;

import epam.model.KnowledgeElement;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RandomStrategy implements QuestionSelectionStrategy {

    private final Random random;

    public RandomStrategy() {
        this(new Random());
    }

    public RandomStrategy(Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public KnowledgeElement selectQuestion(List<KnowledgeElement> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("Knowledge bank is empty");
        }
        return elements.get(random.nextInt(elements.size()));
    }
}
