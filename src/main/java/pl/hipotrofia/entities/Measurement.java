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
    private int weight;
    private int height;
    private int headCircuit;
    private Date dateOfMeasure;

}
