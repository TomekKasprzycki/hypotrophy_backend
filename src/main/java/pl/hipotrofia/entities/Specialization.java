package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nameOfSpecialization;
    @ManyToMany(mappedBy = "specialization")
    private List<Doctor> doctor;

}
