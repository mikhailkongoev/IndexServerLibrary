package search;

import interfaces.lexer.Lexer;
import interfaces.services.SearchService;

import javax.persistence.EntityManager;

import static configuration.Configurations.entityManagerFactory;

public abstract class AbstractSearchService implements SearchService {
    protected Lexer lexer;

    protected EntityManager em = entityManagerFactory.createEntityManager();

    AbstractSearchService(Lexer lexer) {
        this.lexer = lexer;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }
}
