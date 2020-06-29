package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "child_id", referencedColumnName = "id")
    private Children child;
    private boolean isBorn;
    private int weight;
    private int height;
    private LocalDate dateOfMeasure;

}
