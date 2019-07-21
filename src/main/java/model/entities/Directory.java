package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@DiscriminatorValue("1")
public class Directory extends AbstractFile {
    @OneToMany
    @Getter
    @Setter
    private List<AbstractFile> childs;
}
