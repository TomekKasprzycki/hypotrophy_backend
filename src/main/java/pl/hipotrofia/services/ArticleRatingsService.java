package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.ArticleDto;
import pl.hipotrofia.entities.ArticleRatings;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.repositories.ArticleRatingsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleRatingsService {

    private final ArticleRatingsRepository articleRatingsRepository;
    private final ArticlesService articlesService;

    public ArticleRatingsService(ArticleRatingsRepository articleRatingsRepository,
                                 ArticlesService articlesService) {
        this.articleRatingsRepository = articleRatingsRepository;
        this.articlesService=articlesService;
    }

    public Optional<List<ArticleRatings>> getAllByUser(User user) {
        return articleRatingsRepository.findAllByAuthor(user);
    }

    public List<ArticleDto> addUserRatingToArticleDto(List<ArticleDto> articleDtoList, User user) {

        List<ArticleRatings> articleRatings = getAllByUser(user).orElse(new ArrayList<>());

        if (!articleRatings.isEmpty()) {
            articleDtoList.forEach(articleDto -> {
                        articleRatings.forEach(articleRating -> {
                            if (articleRating.getArticle().getId().equals(articleDto.getId())) {
                                articleDto.setUserRating(true);
                            }
                        });
                    }
            );
        }

        return articleDtoList;
    }

    public void addUserRating(Long articleId, User user) {
        Articles article = articlesService.findArticleById(articleId);

        ArticleRatings articleRating = new ArticleRatings();
        articleRating.setArticle(article);
        articleRating.setAuthor(user);

        articleRatingsRepository.save(articleRating);
    }

    public void remove(Long articleId, User author) {
        Articles article = articlesService.findArticleById(articleId);
        articleRatingsRepository.deleteByAuthorAndArticle(author, article);
    }

    public int countByArticle(Articles article) {
        return articleRatingsRepository.countByArticle(article);
    }
}
