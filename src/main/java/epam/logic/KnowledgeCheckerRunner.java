package epam.logic;

import epam.logic.strategies.QuestionSelectionStrategy;
import epam.model.KnowledgeBank;
import epam.model.KnowledgeElement;
import epam.persistence.KnowledgeBankPersistence;
import epam.ui.UserInterface;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeCheckerRunner {

    private final KnowledgeBankPersistence persistence;
    private final QuestionSelectionStrategy strategy;
    private final UserInterface ui;

    public KnowledgeCheckerRunner(KnowledgeBankPersistence persistence,
                                  QuestionSelectionStrategy strategy,
                                  UserInterface ui) {
        this.persistence = persistence;
        this.strategy = strategy;
        this.ui = ui;
    }

    public void run() {
        KnowledgeBank bank = persistence.load();
        List<KnowledgeElement> elements = bank.getElements()
                == null ? new ArrayList<>() : bank.getElements();
        Set<String> topics = elements.stream()
                .map(KnowledgeElement::getTopic)
                .filter(t -> t != null && !t.isBlank())
                .collect(Collectors.toSet());
        ui.printWelcome(topics, elements.size());
        while (true) {
            KnowledgeElement element = strategy.selectQuestion(elements);
            ui.printQuestion(element.getQuestion());
            String answer = ui.readAnswer();

            if ("/exit".equals(answer)) {
                break;
            }
            boolean correct = element.getAnswer()
                    != null && element.getAnswer().equalsIgnoreCase(answer);
            if (element.getHistory() == null) {
                element.setHistory(new ArrayList<>());
            }
            element.getHistory().add(correct);
            element.setLastAsked(LocalDateTime.now());

            if (correct) {
                ui.printCorrect();
            } else {
                ui.printIncorrect(element.getAnswer());
            }
        }
        ui.printPerformance(calculatePerformanceText(elements));
        persistence.save(bank);
    }

    private String calculatePerformanceText(List<KnowledgeElement> elements) {
        Map<String, List<KnowledgeElement>> byTopic = elements.stream()
                .collect(Collectors.groupingBy(e -> e.getTopic() == null ? "" : e.getTopic()));
        return byTopic.entrySet().stream()
                .filter(e -> !e.getKey().isBlank())
                .map(entry -> {
                    String topic = entry.getKey();
                    List<Boolean> allAnswers = entry.getValue().stream()
                            .filter(el -> el.getHistory() != null)
                            .flatMap(el -> el.getHistory().stream())
                            .toList();

                    int asked = allAnswers.size();
                    if (asked == 0) {
                        return null;
                    }
                    long correct = allAnswers.stream().filter(Boolean.TRUE::equals).count();
                    int percent = (int) Math.round((correct * 100.0) / asked);
                    return String.format("%s - Asked: %d, Correct: %d, Percentage: %d%%",
                            topic, asked, correct, percent);
                })
                .filter(s -> s != null)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining("\n"));
    }
}
