package interfaces.lexer;

import java.util.Collection;

public interface Lexer {
    Collection<String> parse(String query);
}
