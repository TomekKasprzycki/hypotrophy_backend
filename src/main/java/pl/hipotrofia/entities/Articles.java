package pl.hipotrofia.entities;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Articles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Column(columnDefinition = "varchar(100)")
    private String title;
    @NotNull
    @Column(columnDefinition = "varchar(5000)") //we need to set good length of text - max 65k of characters
    private String contents;
    @NotNull
    private Date created;
    @ManyToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<User> authors;
    @OneToMany(mappedBy = "article")
    private List<ArticleModification> changes;
    @ManyToMany(mappedBy = "article")
    private List<Tag> tag;
    @OneToMany(mappedBy = "article")
    private List<Message> messages;
    private int page;
    private int priority; //on creation set value on 0
    private int rating; //on creation set value on 0
    private boolean visible;

}
