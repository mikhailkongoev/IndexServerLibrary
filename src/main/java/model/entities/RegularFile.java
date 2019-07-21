package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("2")
public class RegularFile extends AbstractFile {

    public enum FileFormat {
        TXT, PDF, DOC;
    }

    @Column
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private FileFormat fileFormat;

    @OneToOne(cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Document document;
}
