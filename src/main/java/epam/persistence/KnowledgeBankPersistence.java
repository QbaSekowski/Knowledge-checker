package epam.persistence;

import epam.model.KnowledgeBank;

public interface KnowledgeBankPersistence {

    KnowledgeBank load();

    void save(KnowledgeBank knowledgeBank);
}
