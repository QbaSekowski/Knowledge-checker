package epam.logic.strategies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import epam.model.KnowledgeElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixedStrategyTest {
    private List<KnowledgeElement> elements;

    @BeforeEach
    public void setUp() {
        elements = Arrays.asList(
                new KnowledgeElement(1, "Geography",
                        "What is the capital of Germany?", "Berlin"),
                new KnowledgeElement(2, "History",
                        "When did Christopher Columbus discover the Americas?", "1492")
        );
    }

    @Test
    public void testSelectValidQuestionIndex() {
        // Given
        FixedStrategy strategy = new FixedStrategy(0);

        // When
        KnowledgeElement element = strategy.selectQuestion(elements);

        // Then
        assertNotNull(element);
        assertEquals(1, element.getId(), "Should select the first question");
    }

    @Test
    public void testSelectOutOfBoundsIndex() {
        // Given
        FixedStrategy strategy = new FixedStrategy(2);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> strategy.selectQuestion(elements));
    }

    @Test
    public void testSelectFromEmptyKnowledgeBank() {
        // Given
        List<KnowledgeElement> elements = Collections.emptyList();
        FixedStrategy strategy = new FixedStrategy(0);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> strategy.selectQuestion(elements));
    }
}
