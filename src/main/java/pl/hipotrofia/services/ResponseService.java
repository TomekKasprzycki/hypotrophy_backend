package pl.hipotrofia.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.hipotrofia.converters.ArticleDtoConverter;
import pl.hipotrofia.dto.ArticleDto;
import pl.hipotrofia.dto.response.ArticleListResponseDto;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.myExceptions.UserNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    private final ArticlesService articlesService;
    private final ArticleDtoConverter articleDtoConverter;
    private final UserService userService;
    private final ArticleRatingsService articleRatingsService;

    public ResponseService(ArticlesService articlesService, ArticleDtoConverter articleDtoConverter, UserService userService, ArticleRatingsService articleRatingsService) {
        this.articlesService = articlesService;
        this.articleDtoConverter = articleDtoConverter;
        this.userService = userService;
        this.articleRatingsService = articleRatingsService;
    }

    public ArticleListResponseDto createArticleResponse(int page, int limit, int offset) throws UserNotFoundException {

        int numberOfArticles = articlesService.countArticlesByPage(page);

        List<ArticleDto> listOfArticles = articleDtoConverter.convertToDto(Objects.requireNonNull(articlesService.findArticlesByPages(page, limit, offset).orElse(null)));

        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (email != null) {

                User user = userService.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("dsds"));
                listOfArticles = articleRatingsService.addUserRatingToArticleDto(listOfArticles, user);

        }

        listOfArticles = listOfArticles.stream()
                                       .sorted(Comparator.comparing(ArticleDto::getPriority)
                                               .thenComparing(ArticleDto::getRanking)
                                               .reversed())
                                       .collect(Collectors.toList());

        ArticleListResponseDto responseDto = new ArticleListResponseDto();
        responseDto.setArticleDtoList(listOfArticles);
        responseDto.setCountAllArticles(numberOfArticles);

        return responseDto;
    }

}
