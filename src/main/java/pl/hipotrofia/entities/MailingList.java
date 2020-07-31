package pl.hipotrofia.entities;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class MailingList {

    //1 - ADMINS_MAILING - admin only
    //2 - PUBLISHERS_MAILING - not users only
    //3 - USERS_MAILING - subscribed users
    //4 - ALL_USERS - to all users, in case of emergency :)

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String name;
    @ManyToMany(mappedBy = "mailingList")
    private List<User> users;

}
