package impl.lexer;

import interfaces.lexer.Lexer;

import java.util.Arrays;
import java.util.Collection;

/**
 * Токенайзер, который разбивает содержимое файла или запрос по пробельным символам.
 */
public class SimpleTokenizer implements Lexer {
    @Override
    public Collection<String> parse(String query) {
        return Arrays.asList(query.trim().split("\\s+"));
    }
}
