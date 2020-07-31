package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/anonymous/allToPage/{limit}/{offset}")
    public List<ArticleDto> getArticlesToPage(@RequestParam int page, @PathVariable int limit,
                                              @PathVariable int offset) {

        return articleDtoConverter.convertToDto(articlesService.findArticlesByPages(page, limit, offset));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/notVisible")
    public List<ArticleDto> getAllNotVisible() {

        return articleDtoConverter.convertToDto(articlesService.findAllForAdmin());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/setVisible")
    public List<ArticleDto> setVisible(@RequestParam Long id) {

        Articles article = articlesService.findArticleById(id);
        article.setVisible(true);
        articlesService.addArticle(article);

        return articleDtoConverter.convertToDto(articlesService.findAllForAdmin());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/add")
    public boolean addArticle(@RequestBody ArticleDto articleDto) {

        boolean result = false;
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        long millis = System.currentTimeMillis();
        Articles article = articleDtoConverter.convertFromDto(articleDto);
        article.setVisible(false); //it should be set on false on the frontend
        article.setPriority(0); //it should be set on 0 on the frontend
        article.setRating(0); //it should be set on 0 on the frontend
        article.setCreated(new Date(millis));

        try {
            //we need to decide how we'll name the pages --> my proposal 1 - children's history
            if (role.equals("[ROLE_USER]") && article.getPage() != 1) {
                return false;
            }
            articlesService.addArticle(article);
            result = true;

            //TODO send email to AdminMailingList

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @DeleteMapping("/delete")
    public boolean deleteArticle(@RequestBody ArticleDto articleDto) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        boolean result = false;

        try {
            final Articles article = articleDtoConverter.convertFromDto(articleDto);

            if (role.equals("[ROLE_ADMIN]") || userName.equals(article.getAuthor().getEmail())) {
                articlesService.removeArticle(article);
            }

            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/edit")
    public boolean editArticle(@RequestBody ArticleDto articleDto) {

        boolean result = false;
        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        long millis = System.currentTimeMillis();
        Articles article = articleDtoConverter.convertFromDto(articleDto);
        ArticleModification articleModification = new ArticleModification();
        articleModification.setArticle(article);
        articleModification.setDateOfModification(new Date(millis));
        articleModification.setModifiedBy(userService.findUserByEmail(articleDto.getModifiedBy()));
        List<ArticleModification> changes = article.getChanges() != null ? article.getChanges() : new ArrayList<>();
        changes.add(articleModification);
        article.setChanges(changes);

        try {
            if (role.equals("[ROLE_ADMIN]") || userName.equals(article.getAuthor().getEmail())) {
                articlesService.addArticle(article);
                //TODO send email to AdminMailingList
            }

            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
