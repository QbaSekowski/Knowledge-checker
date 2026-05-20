package epam;

import epam.config.AppConfig;
import epam.logic.KnowledgeCheckerRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx
                     = new AnnotationConfigApplicationContext(AppConfig.class)) {
            ctx.getBean(KnowledgeCheckerRunner.class).run();
        }
    }
}
