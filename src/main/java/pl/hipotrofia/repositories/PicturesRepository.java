package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Pictures;

import java.util.List;

@Transactional
@Repository
public interface PicturesRepository extends JpaRepository<Pictures, Long> {

    @Query("select p from Pictures p where p.article.id=:id and p.typeOfPhoto=:type order by p.position asc")
    List<Pictures> findAllByArticleId(@Param("id") Long id, @Param("type") int typeOfPhoto);

    @Query("delete from Pictures p where p.article.id=:articleId and p.position=:position")
    void deleteByArticleAndPosition(@Param("articleId") Long articleId,@Param("position") int position);

    @Query("select p from Pictures p where p.article.id=:articleId and p.position=:position")
    List<Pictures> findAllByArticleAndPosition(@Param("articleId") Long articleId,@Param("position") int position);
}
