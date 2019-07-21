package model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NamedQuery(name = "InsignificantWord.findAllInsignificantWords", query = "from InsignificantWord")
public class InsignificantWord {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private long id;

    @Column
    @Getter
    @Setter
    private String value;
}
