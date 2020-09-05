package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.ArticleDtoConverter;
import pl.hipotrofia.dto.ArticleDto;
import pl.hipotrofia.entities.ArticleModification;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.services.ArticlesService;
import pl.hipotrofia.services.MailingListService;
import pl.hipotrofia.services.MailingService;
import pl.hipotrofia.services.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleDtoConverter articleDtoConverter;
    private final ArticlesService articlesService;
    private final MailingService mailingService;
    private final UserService userService;
    private final MailingListService mailingListService;

    public ArticleController(ArticlesService articlesService,
                             ArticleDtoConverter articleDtoConverter,
                             MailingService mailingService,
                             UserService userService,
                             MailingListService mailingListService) {
        this.articleDtoConverter = articleDtoConverter;
        this.articlesService = articlesService;
        this.mailingService = mailingService;
        this.userService = userService;
        this.mailingListService=mailingListService;
    }


    @GetMapping("/anonymous/allToPage/{page}/{limit}/{offset}")
    public List<ArticleDto> getArticlesToPage(@PathVariable int page, @PathVariable int limit,
                                              @PathVariable int offset) {

        List<Articles> articles = null;
        try {
            articles = articlesService.findArticlesByPages(page, limit, offset);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
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
                response.setStatus(404);
            } else {
                Articles article = articleDtoConverter.convertFromDto(articleDto);
                article.setVisible(false); //it should be set on false on the frontend
                article.setPriority(0); //it should be set on 0 on the frontend
                article.setRating(0); //it should be set on 0 on the frontend
                article.setCreated(new Date(millis));
                articlesService.addArticle(article);
                response.setStatus(201);
                Map<String, String> adminEmailList = mailingListService.getMailingList(1L);

                String subject = "Nowy artykuł";
                String contents = "Proszę o recenzję artykułu o tytule " + article.getTitle();

                adminEmailList.keySet().forEach(email -> {
                    try {
                        mailingService.sendMail(email, subject,
                                adminEmailList.get(email) + "!" + "\n\n"
                                        + contents +"\n\n"
                                        + "Pozdrawiam," + "\n"
                                        + "Backend",
                                false);
                        response.setHeader("MAILING", "Success");
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        response.setHeader("MAILING", e.getCause().getMessage());
                    }

                });
                //TODO send email to AdminMailingList
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
                List<ArticleModification> changes = article.getChanges() != null ? article.getChanges() : new ArrayList<>();
                changes.add(articleModification);
                article.setChanges(changes);
                articlesService.addArticle(article);
                response.setStatus(200);
                //TODO send email to AdminMailingList

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
