package model.embeddable;

import lombok.Getter;
import model.entities.Document;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 * Primary key для {@link model.entities.IndexLine}
 */
@Embeddable
public class IndexLinePK implements Serializable {
    /**
     * Ключевое слово
     */
    @Column
    @Getter
    private String keyword;

    /**
     * Проиндексированный документ
     */
    @OneToOne
    @Getter
    private Document document;

    public IndexLinePK() {

    }

    public IndexLinePK(String keyword, Document document) {
        this.keyword = keyword;
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexLinePK that = (IndexLinePK) o;
        return Objects.equals(keyword, that.keyword) &&
                Objects.equals(document, that.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, document);
    }
}
