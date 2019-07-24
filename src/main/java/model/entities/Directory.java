package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Директория файловой системы, сохранённая в индексе.
 */
@Entity
@DiscriminatorValue("1")
public class Directory extends AbstractFile {
    /**
     * Файлы и директории, лежащие непосредственно в текущей директории
     */
    @OneToMany
    @Getter
    @Setter
    private List<AbstractFile> childs;
}
