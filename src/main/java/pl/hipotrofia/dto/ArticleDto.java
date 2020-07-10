package pl.hipotrofia.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ArticleDto {

    private Long id;
    private String title;
    private String contents;
    private Date created;
    private Map<Long, String> authors; //tu musi być <set> lub <map> id i name
    private List<Long> tagsId;
    private String modifiedBy;
    private int page;
    private int ranking;
    private int priority;
    private boolean visible;

}
