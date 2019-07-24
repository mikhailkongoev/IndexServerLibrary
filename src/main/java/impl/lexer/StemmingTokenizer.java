package impl.lexer;

import interfaces.lexer.Lexer;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Токенайзер, который каждое слово приводит к начальной форме.
 */
public class StemmingTokenizer implements Lexer {
    @Override
    public Collection<String> parse(String query) {
        return Arrays.stream(query.trim().split("\\s+")).map(this::stemStr).collect(Collectors.toList());
    }

    /**
     * @param keyword Исходное ключевое слово
     * @return Приведённое к начальной форме ключевое слово.
     */
    private String stemStr(String keyword) {
        //TODO: implement
        return keyword;
    }
}
