package model.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("2")
public class RegularFile extends AbstractFile {
    @ManyToOne
    private FileFormat fileFormat;
}
