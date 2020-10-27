package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ArticleRatings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User author;
    @ManyToOne
    private Articles article;

}
