package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("2")
public class RegularFile extends AbstractFile {
    @ManyToOne
    @Getter
    @Setter
    private FileFormat fileFormat;
}
