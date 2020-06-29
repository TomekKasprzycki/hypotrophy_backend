package pl.hipotrofia.entities;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "varchar(50)")
    private String name;
    @NotNull
    @Column(columnDefinition = "varchar(50)")
    private String email;
    @NotNull
    private String password;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
    @NotNull
    private LocalDate created;
    private LocalDate passwordChange;
    @ManyToMany(mappedBy = "author")
    private List<Articles> article;
    @OneToMany(mappedBy = "author")
    private List<Message> messages;
    @OneToMany(mappedBy = "user")
    private List<Children> children;


    public void setPassword(String unHashedPassword) {
        this.password = BCrypt.hashpw(unHashedPassword, BCrypt.gensalt());
    }

    public Boolean checkPassword(String unHashedPassword) {
        return BCrypt.checkpw(unHashedPassword, this.password);
    }

}
