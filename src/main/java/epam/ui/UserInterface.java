package epam.ui;

import java.util.Set;

public interface UserInterface {
    void printWelcome(Set<String> topics, int totalQuestions);

    void printQuestion(String question);

    String readAnswer();

    void printCorrect();

    void printIncorrect(String correctAnswer);

    void printPerformance(String performanceText);
}
