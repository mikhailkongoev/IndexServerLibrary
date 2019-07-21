package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FileFormat {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private long id;

    @Column
    @Getter
    @Setter
    private String format;
}
