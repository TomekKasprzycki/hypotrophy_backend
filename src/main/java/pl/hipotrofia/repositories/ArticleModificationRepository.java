package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.ArticleModification;
import pl.hipotrofia.entities.Articles;

import java.util.List;

@Repository
@Transactional
public interface ArticleModificationRepository extends JpaRepository<ArticleModification, Long> {

    @Query("select am from ArticleModification am where am.article=:article")
    List<ArticleModification> getAllByArticle(@Param("article") Articles article);
}
