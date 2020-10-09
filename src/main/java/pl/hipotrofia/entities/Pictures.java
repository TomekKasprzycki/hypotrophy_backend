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
    private String fileName;
    private int position;
    private int typeOfPhoto; //1 - full size, 2 - mobile, 3 - miniature
    @ManyToOne
    @JoinColumn(name = "articles_id", referencedColumnName = "id")
    private Articles article;

}
