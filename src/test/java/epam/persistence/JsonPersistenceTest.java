package epam.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import epam.model.KnowledgeBank;
import epam.model.KnowledgeElement;
import java.util.List;
import org.junit.jupiter.api.Test;

class JsonPersistenceTest {

    @Test
    void testLoadReadsPredefinedJsonFileCorrectly() {
        // Arrange: use the main data file for this test
        String testFilePath = "test_data/knowledge.json";
        JsonPersistence persistence = new JsonPersistence(testFilePath);

        // Act
        KnowledgeBank bank = persistence.load();
        List<KnowledgeElement> elements = bank.getElements();

        // Assert
        assertEquals(3, elements.size(), "Should load 3 elements from JSON");

        // --- First KnowledgeElement ---
        KnowledgeElement first = elements.get(0);
        assertAll("KnowledgeElement 1: " + first,
                () -> assertEquals(1, first.getId(), "id"),
                () -> assertEquals("What is the capital of Germany?",
                        first.getQuestion(), "question"),
                () -> assertEquals("Berlin", first.getAnswer(), "answer"),
                () -> assertEquals("Geography", first.getTopic(), "topic"),
                () -> assertNull(first.getLastAsked(), "lastAsked"),
                () -> assertEquals(0, first.getHistory().size(), "history size")
        );

        // --- Second KnowledgeElement ---
        KnowledgeElement second = elements.get(1);
        assertAll("KnowledgeElement 2: " + second,
                () -> assertEquals(2, second.getId(), "id"),
                () -> assertEquals("When did Christopher Columbus discover the Americas",
                        second.getQuestion(), "question"),
                () -> assertEquals("1492", second.getAnswer(), "answer"),
                () -> assertEquals("History", second.getTopic(), "topic"),
                () -> assertNotNull(second.getLastAsked(), "lastAsked"),
                () -> assertEquals(3, second.getHistory().size(), "history size"),
                () -> assertEquals(Boolean.FALSE, second.getHistory().get(0),
                        "history first element")
        );

        // --- Third KnowledgeElement ---
        KnowledgeElement third = elements.get(2);
        assertAll("KnowledgeElement 3: " + third,
                () -> assertEquals(3, third.getId(), "id"),
                () -> assertEquals("Who is the director "
                                + "and writer of movie Star Wars 'A New Hope'?",
                        third.getQuestion(), "question"),
                () -> assertEquals("George Lucas", third.getAnswer(), "answer"),
                () -> assertEquals("Culture", third.getTopic(), "topic"),
                () -> assertNotNull(third.getLastAsked(), "lastAsked"),
                () -> assertEquals(1, third.getHistory().size(), "history size"),
                () -> assertEquals(Boolean.FALSE, third.getHistory().get(0),
                        "history first element")
        );
    }
}
