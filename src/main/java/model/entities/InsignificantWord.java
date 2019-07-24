package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Незначительное слово, которое должно быть опущено при индексации/поиске с использованием {@link impl.lexer.FilteringTokenizer}
 * Примерами могут являться предлоги, союзы, артикли, аббревиатуры.
 */
@Entity
@NamedQuery(name = "InsignificantWord.findAllInsignificantWords", query = "from InsignificantWord")
public class InsignificantWord {
    /**
     * Уникальный идентификатор
     */
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private long id;

    /**
     * Слово, которое не должно рассматриваться при использовании {@link impl.lexer.FilteringTokenizer}
     */
    @Column
    @Getter
    @Setter
    private String value;
}
