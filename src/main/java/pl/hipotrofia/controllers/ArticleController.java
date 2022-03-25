package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.ArticleDtoConverter;
import pl.hipotrofia.dto.ArticleDto;
import pl.hipotrofia.entities.ArticleModification;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.myExceptions.UserNotFoundException;
import pl.hipotrofia.services.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleDtoConverter articleDtoConverter;
    private final ArticlesService articlesService;
    private final MailingService mailingService;
    private final UserService userService;
    private final ArticleModificationService articleModificationService;
    private final ArticleRatingsService articleRatingsService;

    public ArticleController(ArticlesService articlesService,
                             ArticleDtoConverter articleDtoConverter,
                             MailingService mailingService,
                             UserService userService,
                             ArticleModificationService articleModificationService,
                             ArticleRatingsService articleRatingsService) {
        this.articleDtoConverter = articleDtoConverter;
        this.articlesService = articlesService;
        this.mailingService = mailingService;
        this.userService = userService;
        this.articleModificationService = articleModificationService;
        this.articleRatingsService = articleRatingsService;
    }


    @GetMapping("/anonymous/allToPage")

    public List<ArticleDto> getArticlesToPage(@RequestParam int page, @RequestParam int limit,
                                              @RequestParam int offset, HttpServletResponse response) throws UserNotFoundException {

        List<Articles> articles = articlesService
                .findArticlesByPages(page, limit, offset)
                .orElse(new ArrayList<>());

        List<ArticleDto> articleDtoList = articleDtoConverter.convertToDto(articles);

        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (!email.equals("anonymousUser")) {

                User user = userService.findUserByEmail(email).orElseThrow(() ->
                    new UserNotFoundException("User not found"));
                articleDtoList = articleRatingsService.addUserRatingToArticleDto(articleDtoList, user);

        }
        articleDtoList = articleDtoList.stream()
                .sorted(Comparator.comparing(ArticleDto::getPriority).thenComparing(ArticleDto::getRanking).reversed())
                .collect(Collectors.toList());


        return articleDtoList;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/notVisible")
    public List<ArticleDto> getAllNotVisible() {

        return articleDtoConverter.convertToDto(articlesService.findAllForAdmin());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/setVisible")
    public void setVisible(@RequestParam Long id) {

        Articles article = articlesService.findArticleById(id);
        article.setVisible(true);
        articlesService.addArticle(article);

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/add")
    public void addArticle(@RequestBody ArticleDto articleDto, HttpServletResponse response) {

        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        long millis = System.currentTimeMillis();


            if (role.equals("[ROLE_USER]") && articleDto.getPage() != 2) {
                response.setStatus(403);
                response.setHeader("ERROR","ONLY FOR CREATOR");
            } else {
                Articles article = articleDtoConverter.convertFromDto(articleDto);
                article.setVisible(false); //it should be set on false on the frontend
                article.setPriority(0); //it should be set on 0 on the frontend
                article.setCreated(new Date(millis));
                articlesService.addArticle(article);

                String subject = "Nowy artykuł";
                String contents = "Proszę o recenzję artykułu o tytule " + article.getTitle();
                //todo link to new article
                mailingService.sendEmailToAdmin(response, subject, contents);

            }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @DeleteMapping("/delete")
    public void deleteArticle(@RequestBody ArticleDto articleDto, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        try {
            final Articles article = articlesService.getById(articleDto.getId()).orElseThrow(NullPointerException::new);

            if (role.equals("[ROLE_ADMIN]") || userName.equals(article.getAuthor().getEmail())) {
                articlesService.removeArticle(article);
                response.setStatus(200);
            } else {
                response.setStatus(403);
            }

        } catch (NullPointerException ex) {
          response.setStatus(404);
          response.setHeader("ERROR", "Brak takiego artykułu w bazie danych!");
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

            Articles article = articleDtoConverter.convertFromDto(articleDto);

            if (role.equals("[ROLE_ADMIN]") || userName.equals(article.getAuthor().getEmail())) {

                long millis = System.currentTimeMillis();

                ArticleModification articleModification = new ArticleModification();
                articleModification.setArticle(article);
                articleModification.setDateOfModification(new Date(millis));
                articleModification.setModifiedBy(userService.findUserByEmail(articleDto.getModifiedBy()).orElse(null));
                articleModificationService.add(articleModification);

                //is this necessary??
//                List<ArticleModification> changes = article.getChanges() != null ? article.getChanges() : new ArrayList<>();
//                article.setChanges(changes);
                article.setVisible(false);
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
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @GetMapping("/increaseRating")
    public void increaseRating(@RequestParam Long articleId, HttpServletResponse response) {

        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        try {
            User user = userService.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("Some error"));
            articleRatingsService.addUserRating(articleId, user);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setHeader("ERROR", ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @GetMapping("/decreaseRating")
    public void decreaseRating(@RequestParam Long articleId, HttpServletResponse response) throws UserNotFoundException {

        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

            User user = userService.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("Some error"));
            articleRatingsService.remove(articleId, user);

    }

}
