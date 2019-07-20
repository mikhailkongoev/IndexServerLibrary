package interfaces.services;

import interfaces.lexer.Lexer;

public interface ConfigurationService {
    void setLexer(Lexer lexer);

    IndexService createIndexService();

    SearchService createSearchService();
}
