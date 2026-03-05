package epam.logic.strategies;

import epam.model.KnowledgeElement;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AdaptiveStrategy implements QuestionSelectionStrategy {

    private final Random random;

    public AdaptiveStrategy() {
        this(new Random());
    }

    public AdaptiveStrategy(Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public KnowledgeElement selectQuestion(List<KnowledgeElement> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("Knowledge bank is empty");
        }
        List<KnowledgeElement> sorted = elements.stream()
                .sorted(Comparator.comparingInt(this::calculateTotalWeight).reversed())
                .toList();
        int limit = Math.min(5, sorted.size());
        List<KnowledgeElement> top = sorted.subList(0, limit);
        return top.get(random.nextInt(top.size()));
    }

    int calculateTotalWeight(KnowledgeElement element) {
        return calculateHistoryWeight(element) + calculateTimeWeight(element);
    }

    int calculateHistoryWeight(KnowledgeElement element) {
        if (element == null || element.getHistory() == null || element.getHistory().isEmpty()) {
            return 0;
        }
        List<Boolean> history = element.getHistory();
        int start = Math.max(history.size() - 2, 0);
        int weight = 0;
        for (int i = start; i < history.size(); i++) {
            Boolean correct = history.get(i);
            if (Boolean.TRUE.equals(correct)) {
                weight -= 1;
            } else {
                weight += 2;
            }
        }
        return weight;
    }

    int calculateTimeWeight(KnowledgeElement element) {
        return (element == null || element.getLastAsked() == null) ? 1 : 0;
    }
}
