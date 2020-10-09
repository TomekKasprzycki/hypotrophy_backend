package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "varchar(500)")
    private String token;
    private boolean active;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;

}
