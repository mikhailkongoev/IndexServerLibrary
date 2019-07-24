package impl.lexer;

import interfaces.lexer.Lexer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Токенайзер, который ищет не только по самим словам, но и по их синонимам. Использует словарь синонимов.
 */
public class SynonymTokenizer implements Lexer {
    @Override
    public Collection<String> parse(String query) {
        return Arrays.stream(query.trim().split("\\s+")).flatMap(k -> getSynonyms(k).stream()).collect(Collectors.toList());
    }

    /**
     * @param keyword Ключевое клово
     * @return Коллекция слов синонимов, включая исходное слово
     */
    private Collection<String> getSynonyms(String keyword) {
        //TODO: implement
        return Collections.singletonList(keyword);
    }
}
