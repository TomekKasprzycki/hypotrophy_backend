package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.ArticleRatings;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ArticleRatingsRepository extends JpaRepository<ArticleRatings, Long> {

    Optional<List<ArticleRatings>> findAllByAuthor(User user);

    void deleteByAuthorAndArticle(User author, Articles article);

    int countByArticle(Articles article);
}
