package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Проиндексированный файл файловой системы, может представлять из себя либо документ, либо директорию.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DTYPE", discriminatorType = DiscriminatorType.INTEGER)
@NamedQuery(name = "AbstractFile.findAllFilesByPath", query = "from AbstractFile where fullPath like CONCAT(:path, '%')")
public abstract class AbstractFile {
    /**
     * Абсолютный путь до файла
     */
    @Id
    @Column
    @Getter
    @Setter
    private String fullPath;

    /**
     * Директория, в которой содержится текущий файл или директория. null, если родитель не проиндексирован.
     */
    @ManyToOne
    @Getter
    @Setter
    private Directory parent;
}
