package interfaces.services;

import interfaces.lexer.Lexer;

import java.io.IOException;

public interface ConfigurationService {
    void setLexer(Lexer lexer);

    IndexService createIndexService() throws IOException;

    SearchService createSearchService();
}
