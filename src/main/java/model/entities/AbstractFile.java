package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DTYPE", discriminatorType = DiscriminatorType.INTEGER)
public abstract class AbstractFile {
    @Id
    @Column
    @Getter
    @Setter
    private String fullPath;

    @ManyToOne
    @Getter
    @Setter
    private Directory parent;
}
