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
    private List<String> authors;
    private List<Long> tagsId;
    private int page;

}
