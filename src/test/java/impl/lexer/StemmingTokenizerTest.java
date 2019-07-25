package impl.lexer;

import interfaces.lexer.Lexer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class StemmingTokenizerTest {

    @Test
    void parse() {
        Lexer lexer = new StemmingTokenizer();

        String query = "    his      program made   the world    better   ";
        Collection<String> expectedKeywords = Arrays.asList("he", "program", "make", "the", "world", "good");
        Collection<String> actualKeywords = lexer.parse(query);
        assertEquals(expectedKeywords, actualKeywords);
        assertNotSame(expectedKeywords, actualKeywords);
    }
}