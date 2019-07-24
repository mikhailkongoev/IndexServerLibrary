package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Файл (не директория), сохранённый в индексе. Из него может быть изъято содержимое в текстовом формате.
 * TODO: add metadata extraction and persistence (author, modified_date, etc.)
 */
@Entity
@DiscriminatorValue("2")
public class RegularFile extends AbstractFile {

    public enum FileFormat {
        TXT, PDF, DOC;
    }

    /**
     * Формат файла. Библиотекой поддерживаются только заданный список возможных расширений файлов.
     */
    @Column
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private FileFormat fileFormat;

    /**
     * Проиндексированный документ, соответствующий текущему файлу.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Document document;
}
