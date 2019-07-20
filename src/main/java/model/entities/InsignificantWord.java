package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "InsignificantWord.findAllInsignificantWords", query = "from InsignificantWord")
public class InsignificantWord {
    @Id
    @Getter
    @Setter
    private long id;

    @Column
    @Getter
    @Setter
    private String value;
}
