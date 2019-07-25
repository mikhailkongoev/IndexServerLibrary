package impl.lexer;

import interfaces.lexer.Lexer;
import model.entities.InsignificantWord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static impl.configuration.Configurations.em;
import static org.junit.jupiter.api.Assertions.*;

class FilteringTokenizerTest {

    private static InsignificantWord aArticle;
    private static InsignificantWord theArticle;

    @BeforeAll
    static void setUp() {
        em.getTransaction().begin();
        aArticle = new InsignificantWord("a");
        theArticle = new InsignificantWord("the");
        em.persist(aArticle);
        em.persist(theArticle);
        em.getTransaction().commit();
    }

    @AfterAll
    static void tearDown() {
        em.getTransaction().begin();
        em.remove(aArticle);
        em.remove(theArticle);
        em.getTransaction().commit();
    }

    @Test
    void parse() {
        Lexer lexer = new FilteringTokenizer();

        String query = "    a      person who   made the    world   better";
        Collection<String> expectedKeywords = Arrays.asList("person", "who", "made", "world", "better");
        Collection<String> actualKeywords = lexer.parse(query);
        assertEquals(expectedKeywords, actualKeywords);
        assertNotSame(expectedKeywords, actualKeywords);
    }
}