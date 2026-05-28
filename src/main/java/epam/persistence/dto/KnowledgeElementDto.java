package epam.persistence.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

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
