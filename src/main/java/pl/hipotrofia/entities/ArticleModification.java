package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class ArticleModification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User modifiedBy;
    private Date dateOfModification;
    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Articles article;

}
