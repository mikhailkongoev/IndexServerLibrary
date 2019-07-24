package impl.search;

import interfaces.lexer.Lexer;
import interfaces.services.SearchService;

/**
 * Предок всех {@link SearchService}
 * Хранит в себе используемый {@link Lexer}
 */
public abstract class AbstractSearchService implements SearchService {
    /**
     * Применяемый для поиска лексер.
     */
    protected Lexer lexer;

    AbstractSearchService(Lexer lexer) {
        this.lexer = lexer;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }
}
