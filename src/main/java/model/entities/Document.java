package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Проиндексированный документ.
 */
@Entity
@NamedQuery(name = "Document.findDocumentsByFileList", query = "from Document where file in :filesList")
public class Document {

    /**
     * Уникальный идентификатор.
     */
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private long id;

    /**
     * Имя документа. Соответствует абсолютному пути файла, соответствующему этому документу.
     */
    @Column
    @Getter
    @Setter
    private String name;

    /**
     * Файл в файловой системе, соответствующий текущему документу.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @Getter
    @Setter
    private RegularFile file;

    /**
     * Ключевые слова, по которым был проиндексирован документ. По этим словам теперь документ может быть найден.
     */
    @OneToMany
    @Getter
    @Setter
    private List<IndexLine> keywords = new ArrayList<>();
}
