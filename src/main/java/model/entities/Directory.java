package model.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@DiscriminatorValue("1")
public class Directory extends AbstractFile {
    @OneToMany
    private List<AbstractFile> childs;
}
