package epam.logic.strategies;

import epam.model.KnowledgeElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SequentialStrategyTest {
    private SequentialStrategy strategy;
    private List<KnowledgeElement> elements;

    @BeforeEach
    public void setUp() {
        strategy = new SequentialStrategy();
        elements = Arrays.asList(
                new KnowledgeElement(1, "Geography",
                        "What is the capital of Germany?", "Berlin"),
                new KnowledgeElement(2, "History",
                        "When did Christopher Columbus discover the Americas?", "1492")
        );
    }

    @Test
    public void testSelectQuestionSequentially() {
        KnowledgeElement firstSelection = strategy.selectQuestion(elements);
        assertNotNull(firstSelection, "Selected question should not be null");
        assertEquals(elements.get(0).getQuestion(), firstSelection.getQuestion(),
                "First selection should return the first question");

        KnowledgeElement secondSelection = strategy.selectQuestion(elements);
        assertNotNull(secondSelection, "Selected question should not be null");
        assertEquals(elements.get(1).getQuestion(), secondSelection.getQuestion(),
                "Second selection should return the second question");

        // Should wrap around
        KnowledgeElement thirdSelection = strategy.selectQuestion(elements);
        assertNotNull(thirdSelection, "Selected question should not be null");
        assertEquals(elements.get(0).getQuestion(), thirdSelection.getQuestion(),
                "Third selection should return the first question");
    }

    @Test
    public void testSelectQuestionWithEmptyKnowledgeBank() {
        // Given
        List<KnowledgeElement> elements = Collections.emptyList();

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> strategy.selectQuestion(elements));
    }
}
