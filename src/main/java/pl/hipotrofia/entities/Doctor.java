package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String city;
    private String zip_code;
    private String region;
    private String street;
    private String building_number;
    @ManyToMany
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    private List<Specialization> specialization;

}
