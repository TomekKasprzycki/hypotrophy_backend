package pl.hipotrofia.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDto {

    private Long id;
    private String contents;
    private String author;
    private Long articleId;
    private Date created;

}
