package impl.lexer;

import interfaces.lexer.Lexer;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class StemmingTokenizer implements Lexer {
    @Override
    public Collection<String> parse(String query) {
        return Arrays.stream(query.trim().split("\\s+")).map(this::stemStr).collect(Collectors.toList());
    }

    private String stemStr(String keyword) {
        //TODO: implement
        return keyword;
    }
}
