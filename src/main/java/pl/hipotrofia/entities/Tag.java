package pl.hipotrofia.entities;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Column(columnDefinition = "varchar(50)", unique = true)
    private String name;
    @ManyToMany
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private List<Articles> article;

}
