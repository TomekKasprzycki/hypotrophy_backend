package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    private boolean active;
    @OneToOne
    private User user;

}
