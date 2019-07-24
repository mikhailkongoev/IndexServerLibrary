package model.entities;

import lombok.Getter;
import lombok.Setter;
import model.embeddable.IndexLinePK;

import javax.persistence.*;

@Entity
@NamedQuery(name = "IndexLine.removeIndexLinesByDocumentsList", query = "delete from IndexLine where indexLinePK.document in :docsList")
public class IndexLine {
    @EmbeddedId
    @Getter
    @Setter
    private IndexLinePK indexLinePK;

    public IndexLine() {

    }

    public IndexLine(String keyword, Document document) {
        indexLinePK = new IndexLinePK(keyword, document);
    }

    @Override
    public String toString() {
        return indexLinePK.getKeyword() + " " + indexLinePK.getDocument().getName();
    }
}
