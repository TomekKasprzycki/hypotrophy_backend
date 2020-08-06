package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class FAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "varchar(1000)")
    private String question;
    @Column(columnDefinition = "varchar(1000)")
    private String answer;

}
