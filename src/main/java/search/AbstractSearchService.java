package search;

import interfaces.lexer.Lexer;
import interfaces.services.SearchService;

public abstract class AbstractSearchService implements SearchService {
    protected Lexer lexer;

    AbstractSearchService(Lexer lexer) {
        this.lexer = lexer;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }
}
