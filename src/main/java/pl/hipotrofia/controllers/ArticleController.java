package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.ArticleDtoConverter;
import pl.hipotrofia.dto.ArticleDto;
import pl.hipotrofia.entities.ArticleModification;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.services.ArticleModificationService;
import pl.hipotrofia.services.ArticlesService;
import pl.hipotrofia.services.MailingService;
import pl.hipotrofia.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleDtoConverter articleDtoConverter;
    private final ArticlesService articlesService;
    private final MailingService mailingService;
    private final UserService userService;
    private final ArticleModificationService articleModificationService;

    public ArticleController(ArticlesService articlesService,
                             ArticleDtoConverter articleDtoConverter,
                             MailingService mailingService,
                             UserService userService,
                             ArticleModificationService articleModificationService) {
        this.articleDtoConverter = articleDtoConverter;
        this.articlesService = articlesService;
        this.mailingService = mailingService;
        this.userService = userService;
        this.articleModificationService=articleModificationService;
    }


    @GetMapping("/anonymous/allToPage/{page}/{limit}/{offset}")
    public List<ArticleDto> getArticlesToPage(@PathVariable int page, @PathVariable int limit,
                                              @PathVariable int offset, HttpServletResponse response) {

        List<Articles> articles = articlesService
                .findArticlesByPages(page, limit, offset)
                .orElseThrow(() -> {
                    NullPointerException ex = new NullPointerException();
                    response.setStatus(404);
                    response.setHeader("ERROR", ex.getLocalizedMessage());
                    return ex;
                });

        return articleDtoConverter.convertToDto(articles);
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
    public void addArticle(@RequestBody ArticleDto articleDto, HttpServletResponse response) {

        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        long millis = System.currentTimeMillis();

        try {
            if (role.equals("[ROLE_USER]") && articleDto.getPage() != 2) {
                response.setStatus(403);
            } else {
                Articles article = articleDtoConverter.convertFromDto(articleDto);
                article.setVisible(false); //it should be set on false on the frontend
                article.setPriority(0); //it should be set on 0 on the frontend
                article.setRating(0); //it should be set on 0 on the frontend
                article.setCreated(new Date(millis));
                articlesService.addArticle(article);
                response.setStatus(201);

                String subject = "Nowy artykuł";
                String contents = "Proszę o recenzję artykułu o tytule " + article.getTitle();

                mailingService.sendEmailToAdmin(response, subject, contents);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @DeleteMapping("/delete")
    public void deleteArticle(@RequestBody ArticleDto articleDto, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        try {
            final Articles article = articleDtoConverter.convertFromDto(articleDto);

            if (role.equals("[ROLE_ADMIN]") || userName.equals(article.getAuthor().getEmail())) {
                articlesService.removeArticle(article);
                response.setStatus(200);
            } else {
                response.setStatus(403);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/edit")
    public void editArticle(@RequestBody ArticleDto articleDto, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        try {

            Articles article = articlesService.getById(articleDto.getId()).orElseThrow(NullPointerException::new);

            if (role.equals("[ROLE_ADMIN]") || userName.equals(article.getAuthor().getEmail())) {

                long millis = System.currentTimeMillis();

                ArticleModification articleModification = new ArticleModification();
                articleModification.setArticle(article);
                articleModification.setDateOfModification(new Date(millis));
                articleModification.setModifiedBy(userService.findUserByEmail(articleDto.getModifiedBy()));
                articleModificationService.add(articleModification);

                List<ArticleModification> changes = article.getChanges() != null ? article.getChanges() : new ArrayList<>();
                article.setChanges(changes);
                articlesService.addArticle(article);

                response.setStatus(200);

                String subject = "Sprawdź edytowany artykuł";
                String contents = "Wprowadzono zmainy do artykułu: " + article.getTitle() + ".<br/>"
                + "Proszę o sprawdzenie artykułu i podjęcie adekwatnych czynności.<br/><br/>"
                        + "Pozdrawaim, <br/>"
                        + "Backed";

                mailingService.sendEmailToAdmin(response, subject, contents);

            } else {
                response.setStatus(403);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            response.setStatus(404);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", ex.getMessage());
        }
    }
}
