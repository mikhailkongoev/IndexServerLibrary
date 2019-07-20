package lexer;

import interfaces.lexer.Lexer;
import model.entities.InsignificantWord;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static configuration.Configurations.em;

public class FilteringTokenizer implements Lexer {
    @Override
    public Collection<String> parse(String query) {
        Collection<String> insignificantWords = em.createNamedQuery(InsignificantWord.class.getSimpleName() + ".findAllInsignificantWords",
                InsignificantWord.class).getResultList().stream().map(InsignificantWord::getValue).collect(Collectors.toList());

        Set<String> allQueryWords = new HashSet<>(Arrays.asList(query.trim().split("\\s+")));
        allQueryWords.removeAll(insignificantWords);
        return allQueryWords;
    }
}
