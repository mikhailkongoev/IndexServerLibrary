package index;

import interfaces.lexer.Lexer;
import interfaces.services.IndexService;

import javax.persistence.EntityManager;

import static configuration.Configurations.entityManagerFactory;

public abstract class AbstractIndexService implements IndexService {
    protected Lexer lexer;

    protected EntityManager em = entityManagerFactory.createEntityManager();

    AbstractIndexService(Lexer lexer) {
        this.lexer = lexer;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }
}
