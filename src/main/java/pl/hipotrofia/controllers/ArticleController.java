package pl.hipotrofia.controllers;

import org.hibernate.HibernateException;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.ArticleDtoConverter;
import pl.hipotrofia.dto.ArticleDto;
import pl.hipotrofia.entities.ArticleModification;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.services.ArticlesService;
import pl.hipotrofia.services.TagService;
import pl.hipotrofia.services.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleDtoConverter articleDtoConverter;
    private final ArticlesService articlesService;
    private final TagService tagService;
    private final UserService userService;

    public ArticleController(ArticlesService articlesService,
                             ArticleDtoConverter articleDtoConverter,
                             TagService tagService,
                             UserService userService) {
        this.articleDtoConverter = articleDtoConverter;
        this.articlesService = articlesService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/allToPage")
    public List<ArticleDto> getArticlesToPage(@RequestParam int page) {

        return articleDtoConverter.convertToDto(articlesService.findArticlesByPages(page));
    }

    @GetMapping("/notVisible")
    public List<ArticleDto> getAllNotVisible() {

        return articleDtoConverter.convertToDto(articlesService.findAllForAdmin());
    }

    @GetMapping("/setVisible")
    public List<ArticleDto> setVisible(@RequestParam Long id){

        Articles article = articlesService.findArticleById(id);
        article.setVisible(true);
        articlesService.addArticle(article);

        return articleDtoConverter.convertToDto(articlesService.findAllForAdmin());
    }

    @PostMapping("/addArticle")
    public List<ArticleDto> addArticle(@RequestBody ArticleDto articleDto) {

        long milis = System.currentTimeMillis();
        Articles article = articleDtoConverter.convertFromDto(articleDto);
        article.setVisible(false); //it should be set on false on the frontend
        article.setPriority(0); //it should be set on 0 on the frontend
        article.setRating(0); //it should be set on 0 on the frontend
        article.setCreated(new Date(milis));
        article.setTag(articleDto.getTagsId().stream().map(tagService::findTagById).collect(Collectors.toList()));
        article.setAuthors(articleDto.getAuthors().keySet().stream().map(userService::findUserById).collect(Collectors.toList()));
        try {
            articlesService.addArticle(article);
            //TODO send email to AdminMailingList
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return articleDtoConverter.convertToDto(articlesService.findArticlesByPages(articleDto.getPage()));
    }

    @DeleteMapping("/delete")
    public List<ArticleDto> deleteArticle(@RequestBody ArticleDto articleDto) {

        try {
            articlesService.removeArticle(articleDtoConverter.convertFromDto(articleDto));
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return articleDtoConverter.convertToDto(articlesService.findArticlesByPages(articleDto.getPage()));
    }

    @PostMapping("/editArticle")
    public List<ArticleDto> editArticle(@RequestBody ArticleDto articleDto) {

        long milis = System.currentTimeMillis();
        Articles article = articleDtoConverter.convertFromDto(articleDto);
        ArticleModification articleModification = new ArticleModification();
        articleModification.setArticle(article);
        articleModification.setDateOfModification(new Date(milis));
        articleModification.setModifiedBy(userService.findUserByEmail(articleDto.getModifiedBy()));
        List<ArticleModification> changes = article.getChanges() != null ? article.getChanges() : new ArrayList<>();
        changes.add(articleModification);
        article.setChanges(changes);

        try {
            articlesService.addArticle(article);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return articleDtoConverter.convertToDto(articlesService.findArticlesByPages(articleDto.getPage()));
    }

}
