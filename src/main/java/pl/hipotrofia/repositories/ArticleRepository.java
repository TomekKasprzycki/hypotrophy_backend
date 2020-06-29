package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Articles;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ArticleRepository extends JpaRepository<Articles, Long> {

    @Query("select a from Articles a where a.page=:page")
    List<Articles> getAllByPage(@Param("page") int page);

    @Query("select a from Articles a join fetch User u where u.email=:author")
    Optional<List<Articles>> getAllByAuthor(@Param("author") String author);
}
