package pl.hipotrofia.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MessageDto {

    private Long id;
    private String contents;
    private String author;
    private Long articleId;
    private LocalDate created;

}
