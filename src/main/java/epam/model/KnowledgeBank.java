package epam.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBank {
    private List<KnowledgeElement> elements;
}
