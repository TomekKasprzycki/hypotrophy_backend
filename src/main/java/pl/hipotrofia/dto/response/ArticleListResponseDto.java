package pl.hipotrofia.dto.response;

import lombok.Data;
import pl.hipotrofia.dto.ArticleDto;

import java.util.List;

//to powinno być zwracane na front
@Data
public class ArticleListResponseDto {

    List<ArticleDto> articleDtoList;
    int countAllArticles;

}
