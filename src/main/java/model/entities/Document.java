package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Document {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private long id;

    @Column
    @Getter
    @Setter
    private String name;

    @OneToOne
    @Getter
    @Setter
    private RegularFile file;
}
