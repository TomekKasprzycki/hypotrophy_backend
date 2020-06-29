package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Children {

    public enum Gender {
        MALE, FEMALE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private Gender gender;
    @OneToMany(mappedBy = "child")
    private List<Measurement> childData;
    private LocalDate dateOfBirth;

}
