package impl.lexer;

import interfaces.lexer.Lexer;
import model.entities.InsignificantWord;

import java.util.*;
import java.util.stream.Collectors;

import static impl.configuration.Configurations.em;

/**
 * Токенайзер, который при индексации и поиске не учитывает {@link InsignificantWord},
 * не интересные нам слова, наподобие артиклей, союзов, предлогов, аббревиатур.
 */
public class FilteringTokenizer implements Lexer {
    @Override
    public Collection<String> parse(String query) {
        Collection<String> insignificantWords = em.createNamedQuery(InsignificantWord.class.getSimpleName() + ".findAllInsignificantWords",
                InsignificantWord.class).getResultList().stream().map(InsignificantWord::getValue).collect(Collectors.toList());

        List<String> allQueryWords = Arrays.stream(query.trim().split("\\s+")).distinct().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        allQueryWords.removeAll(insignificantWords);
        return allQueryWords;
    }
}
