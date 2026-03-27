package epam.ui;

import org.springframework.stereotype.Component;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConsoleUserInterface implements UserInterface {

    private final Scanner scanner;

    public ConsoleUserInterface() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void printWelcome(Set<String> topics, int totalQuestions) {
        String topicsText = topics.stream().sorted().collect(Collectors.joining(", "));
        System.out.println("Welcome to the Knowledge Bank Application!");
        System.out.println("This application will test your knowledge on the following topics: " + topicsText);
        System.out.println("Total questions in the knowledge bank: " + totalQuestions);
        System.out.println("You will be asked questions from various categories.");
        System.out.println("If you want to exit, type '/exit' as your answer.\n");
    }

    @Override
    public void printQuestion(String question) {
        System.out.println("Question: " + question);
        System.out.print("Your answer: ");
    }

    @Override
    public String readAnswer() {
        return scanner.nextLine();
    }

    @Override
    public void printCorrect() {
        System.out.println("Correct! Well done.\n");
    }

    @Override
    public void printIncorrect(String correctAnswer) {
        System.out.println("Incorrect. The correct answer is: " + correctAnswer + "\n");
    }

    @Override
    public void printPerformance(String performanceText) {
        System.out.println("Your performance:");
        System.out.println(performanceText);
    }
}
