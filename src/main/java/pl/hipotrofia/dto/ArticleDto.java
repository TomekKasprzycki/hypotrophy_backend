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
    private String modifiedBy;
    private int page;
    private int rank;
    private int priority;
    private boolean visible;

}
