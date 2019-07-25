package impl.lexer;

import interfaces.lexer.Lexer;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Токенайзер, который разбивает содержимое файла или запрос по пробельным символам.
 */
public class SimpleTokenizer implements Lexer {
    @Override
    public Collection<String> parse(String query) {
        return Arrays.stream(query.trim().split("\\s+")).distinct().filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }
}
