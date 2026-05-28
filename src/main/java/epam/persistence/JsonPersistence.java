package epam.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import epam.model.KnowledgeBank;
import epam.model.KnowledgeElement;
import epam.persistence.dto.KnowledgeElementDto;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JsonPersistence implements KnowledgeBankPersistence {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final String dataFilePath;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public JsonPersistence(String dataFilePath) {
        this(dataFilePath, new ModelMapper());
    }

    @Autowired
    public JsonPersistence(@Value("${data.file}") String dataFilePath,
                           ModelMapper modelMapper) {
        this.dataFilePath = Objects.requireNonNull(dataFilePath, "dataFilePath");
        this.modelMapper = Objects.requireNonNull(modelMapper, "modelMapper");
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public KnowledgeBank load() {
        try {
            File file = new File(dataFilePath);
            if (!file.exists()) {
                return new KnowledgeBank(List.of());
            }
            List<KnowledgeElementDto> dtos = objectMapper.readValue(
                    file,
                    new TypeReference<List<KnowledgeElementDto>>() {
                    }
            );
            List<KnowledgeElement> elements = new ArrayList<>();
            for (KnowledgeElementDto dto : dtos) {
                elements.add(toModel(dto));
            }
            return new KnowledgeBank(elements);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load knowledge bank from: " + dataFilePath, e);
        }
    }

    @Override
    public void save(KnowledgeBank knowledgeBank) {
        Objects.requireNonNull(knowledgeBank, "knowledgeBank");
        try {
            Path path = Path.of(dataFilePath);
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<KnowledgeElementDto> dtos = new ArrayList<>();
            if (knowledgeBank.getElements() != null) {
                for (KnowledgeElement element : knowledgeBank.getElements()) {
                    dtos.add(toDto(element));
                }
            }
            objectMapper.writeValue(path.toFile(), dtos);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save knowledge bank to: " + dataFilePath, e);
        }
    }

    private KnowledgeElement toModel(KnowledgeElementDto dto) {
        if (dto == null) {
            return null;
        }
        KnowledgeElement element = new KnowledgeElement();
        element.setId(dto.getId());
        element.setTopic(dto.getCategory());
        element.setQuestion(dto.getQuestion());
        element.setAnswer(dto.getAnswer());
        if (dto.getLastAsked() == null) {
            element.setLastAsked(null);
        } else {
            element.setLastAsked(LocalDateTime.parse(dto.getLastAsked(), ISO));
        }
        if (dto.getHistory() == null) {
            element.setHistory(new ArrayList<>());
        } else {
            element.setHistory(new ArrayList<>(dto.getHistory()));
        }

        return element;
    }

    private KnowledgeElementDto toDto(KnowledgeElement element) {
        if (element == null) {
            return null;
        }
        KnowledgeElementDto dto = new KnowledgeElementDto();
        dto.setId(element.getId());
        dto.setCategory(element.getTopic());
        dto.setQuestion(element.getQuestion());
        dto.setAnswer(element.getAnswer());
        LocalDateTime lastAsked = element.getLastAsked();
        dto.setLastAsked(lastAsked == null ? null : ISO.format(lastAsked));
        if (element.getHistory() == null) {
            dto.setHistory(new ArrayList<>());
        } else {
            dto.setHistory(new ArrayList<>(element.getHistory()));
        }
        return dto;
    }
}
