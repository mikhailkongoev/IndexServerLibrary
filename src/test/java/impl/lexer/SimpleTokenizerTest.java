package impl.lexer;

import interfaces.lexer.Lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTokenizerTest {

    @org.junit.jupiter.api.Test
    void parse() {
        Lexer lexer = new SimpleTokenizer();

        String query = "    1test1      2test2 3test3   ";
        Collection<String> expectedKeywords = Arrays.asList("1test1", "2test2", "3test3");
        Collection<String> actualKeywords = lexer.parse(query);
        assertEquals(expectedKeywords, actualKeywords);
        assertNotSame(expectedKeywords, actualKeywords);
    }

    @org.junit.jupiter.api.Test
    void shouldHandleRepeats() {
        Lexer lexer = new SimpleTokenizer();

        String query = "test    test        test   test2";
        Collection<String> expectedKeywords = Arrays.asList("test", "test2");
        Collection<String> actualKeywords = lexer.parse(query);
        assertEquals(expectedKeywords, actualKeywords);
        assertNotSame(expectedKeywords, actualKeywords);
    }

    @org.junit.jupiter.api.Test
    void shouldHandleEmtpyQuery() {
        Lexer lexer = new SimpleTokenizer();

        String query = "              ";
        Collection<String> expectedKeywords = new ArrayList<>();
        Collection<String> actualKeywords = lexer.parse(query);
        assertEquals(expectedKeywords, actualKeywords);
        assertNotSame(expectedKeywords, actualKeywords);

        query = "";
        actualKeywords = lexer.parse(query);
        assertEquals(expectedKeywords, actualKeywords);
        assertNotSame(expectedKeywords, actualKeywords);
    }

    @org.junit.jupiter.api.Test
    void shouldFailOnNullQuery() {
        Lexer lexer = new SimpleTokenizer();

        String query = null;

        assertThrows(NullPointerException.class, () -> lexer.parse(query));
    }
}