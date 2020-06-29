package pl.hipotrofia.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ArticleDto {

    private Long id;
    private String title;
    private String contents;
    private LocalDate created;
    private List<String> authors;
    private List<Long> tagsId;
    private int page;

}
