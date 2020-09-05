package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Pictures {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String path;
    @ManyToOne
    @JoinColumn(name = "articles_id", referencedColumnName = "id")
    private Articles article;

}
