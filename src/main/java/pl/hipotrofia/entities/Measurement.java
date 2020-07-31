package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "child_id", referencedColumnName = "id")
    private Children child;
    private Integer weight;
    private Integer height;
    private Integer headCircuit;
    private Date dateOfMeasure;

}
