package epam.persistence.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class KnowledgeElementDto {
    private int id;
    private String category;
    private String question;
    private String answer;
    private String lastAsked;
    private List<Boolean> history;
}
