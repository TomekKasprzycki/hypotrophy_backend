package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Role {

    //1 - ROLE_ADMIN - full permissions
    //2 - ROLE_PUBLISHER - can publish articles, can write messages on blog, can subscribe to mailing list
    //3 - ROLE_USER - can write messages, can subscribe to mailing list - default role

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "varchar(15)")
    private String name;
    @OneToMany(mappedBy = "role")
    List<User> user;

    @Override
    public String toString() {
        return name;
    }
}
