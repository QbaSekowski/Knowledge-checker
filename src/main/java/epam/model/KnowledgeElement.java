package epam.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KnowledgeElement {
    private int id;
    private String topic;
    private String question;
    private String answer;
    private LocalDateTime lastAsked;
    private List<Boolean> history;

    public KnowledgeElement(int id, String topic, String question, String answer) {
        this.id = id;
        this.topic = topic;
        this.question = question;
        this.answer = answer;
    }
}
