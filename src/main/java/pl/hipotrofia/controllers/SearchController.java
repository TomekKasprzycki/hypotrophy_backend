package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.hipotrofia.converters.ArticleDtoConverter;
import pl.hipotrofia.dto.ArticleDto;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.services.ArticlesService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final ArticlesService articlesService;
    private final ArticleDtoConverter articleDtoConverter;

    public SearchController(ArticleDtoConverter articleDtoConverter, ArticlesService articlesService) {
        this.articleDtoConverter = articleDtoConverter;
        this.articlesService = articlesService;
    }

    //criteria builder!!!! - wtedy wystarczy jeden endpoint
    //limit i offset

    @GetMapping("/anonymous/date")

    public List<ArticleDto> searchByDate(@RequestParam String dateFrom, @RequestParam String dateTo) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date latestDate = null;
        Date earliestDate = null;

        try {
            latestDate = formatter.parse(dateTo);
            earliestDate = formatter.parse(dateFrom);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        if (latestDate != null && earliestDate != null) {
            return articleDtoConverter.convertToDto(articlesService.findArticlesBetweenDates(earliestDate, latestDate));
        }

        if (latestDate == null && earliestDate != null) {
            return articleDtoConverter.convertToDto(articlesService.findArticlesFromDate(earliestDate));
        }

        if (latestDate != null && earliestDate == null) {
            return articleDtoConverter.convertToDto(articlesService.findArticlesToDate(latestDate));
        }

        return articleDtoConverter.convertToDto(articlesService.findAll());
    }

    @GetMapping("/anonymous/byAuthor")
    public List<ArticleDto> getArticlesByAuthor(@RequestParam String author) { //author = user.email

        List<Articles> articles = articlesService.findArticlesByAuthor(author).orElse(null);

        return articles != null ? articleDtoConverter.convertToDto(articles) : null;
    }

}
