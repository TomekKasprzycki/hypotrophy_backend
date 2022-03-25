package pl.hipotrofia.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleDto {

    private Long id;
    private String title;
    private String contents;
    private Date created;
    private UserArticleDto author;
    private List<String> tagsName; //można zamienić na String name
    private String modifiedBy;
    private int page; //enum
    private int ranking;
    private int priority;
    private boolean visible;
    private boolean userRating;

}
