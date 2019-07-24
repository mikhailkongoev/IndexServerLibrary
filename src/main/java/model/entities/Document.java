package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name = "Document.findDocumentsByFileList", query = "from Document where file in :filesList")
public class Document {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private long id;

    @Column
    @Getter
    @Setter
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @Getter
    @Setter
    private RegularFile file;

    @OneToMany
    @Getter
    @Setter
    private List<IndexLine> keywords = new ArrayList<>();
}
