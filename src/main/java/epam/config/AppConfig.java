package epam.config;

import epam.logic.strategies.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "epam")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public QuestionSelectionStrategy questionSelectionStrategy(
            @Value("${question.selection.strategy}") String strategyName,
            @Value("${fixedstrategy.questionIndex}") int fixedIndex
    ) {
        String normalized = strategyName == null ? "" : strategyName.trim().toUpperCase();
        return switch (normalized) {
            case "FIXED" -> new FixedStrategy(fixedIndex);
            case "SEQUENTIAL" -> new SequentialStrategy();
            case "RANDOM" -> new RandomStrategy();
            case "ADAPTIVE" -> new AdaptiveStrategy();
            default -> throw new IllegalArgumentException("Unknown strategy: " + strategyName);
        };
    }
}