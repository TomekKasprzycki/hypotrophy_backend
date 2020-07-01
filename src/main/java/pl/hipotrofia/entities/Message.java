package pl.hipotrofia.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "varchar(500)")
    private String contents;
    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Articles article;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User author;
    private Date created;

}
