package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.hipotrofia.converters.ArticleDtoConverter;
import pl.hipotrofia.dto.ArticleDto;
import pl.hipotrofia.services.ArticlesService;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleDtoConverter articleDtoConverter;
    private final ArticlesService articlesService;

    public ArticleController(ArticlesService articlesService, ArticleDtoConverter articleDtoConverter) {
        this.articleDtoConverter = articleDtoConverter;
        this.articlesService = articlesService;
    }

    @GetMapping("/allToPage")
    public List<ArticleDto> getArticlesToPage(@RequestParam int page) {

        return articleDtoConverter.convertToDto(articlesService.findArticlesByPages(page));
    }

}
