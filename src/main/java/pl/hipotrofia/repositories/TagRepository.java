package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Tag;

import java.util.List;

@Repository
@Transactional
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select t from Tag t where t.id=:id")
    Tag getById(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select * from tag inner join tag_article ta on tag.id = ta.tag_id where ta.article_id=:id")
    List<Tag> getAllByArticle(@Param("id") Long id);
}
