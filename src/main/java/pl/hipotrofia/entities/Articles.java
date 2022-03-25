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
    @Column(columnDefinition = "text") // max 65k of characters
    private String contents;
    @NotNull
    private Date created;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User author;
    @OneToMany(mappedBy = "article")
    private List<ArticleModification> changes;
    @ManyToMany(mappedBy = "article", fetch = FetchType.EAGER)
    private List<Tag> tags;
    @OneToMany(mappedBy = "article")
    private List<Message> messages;
    private int page;
    private int priority; //on creation set value on 0
    @OneToMany(mappedBy = "article")
    private List<ArticleRatings> articleRating;
    private boolean visible;
    @OneToMany(mappedBy = "article")
    private List<Pictures> pictures;

    @Override
    public String toString() {
        return "Articles{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
