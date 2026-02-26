package epam.logic.strategies;

import epam.model.KnowledgeElement;

import java.util.List;

public interface QuestionSelectionStrategy {
    KnowledgeElement selectQuestion(List<KnowledgeElement> elements);
}
