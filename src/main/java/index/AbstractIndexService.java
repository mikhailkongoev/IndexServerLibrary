package index;

import interfaces.lexer.Lexer;
import interfaces.services.IndexService;

public abstract class AbstractIndexService implements IndexService {
    protected Lexer lexer;

    AbstractIndexService(Lexer lexer) {
        this.lexer = lexer;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }
}
