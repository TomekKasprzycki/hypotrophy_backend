package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class DocEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "varchar(1000)")
    private String opinion;
    private int evaluation;
    private boolean nfz;
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

}
