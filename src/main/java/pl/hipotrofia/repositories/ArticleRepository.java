package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Articles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ArticleRepository extends JpaRepository<Articles, Long> {

    @Query(nativeQuery = true, value =
            "select * " +
                    "from Articles a " +
                    "where a.visible=true and a.page=:page " +
                    "order by a.priority, a.rating " +
                    "limit :limit " +
                    "offset :offset")
    Optional<List<Articles>> getAllByPage(@Param("page") int page, @Param("limit") int limit, @Param("offset") int offset);

    @Query("select a from Articles a join fetch User u where a.visible=true and u.email=:author")
    Optional<List<Articles>> getAllByAuthor(@Param("author") String author);

    @Query("select a from Articles a where a.visible=true and a.created>=:earliestDate and a.created<=:latestDate")
    List<Articles> getAllBetweenDates(@Param("earliestDate") Date earliestDate, @Param("latestDate") Date latestDate);

    @Query("select a from Articles a where a.visible=true and a.created>=:earliestDate")
    List<Articles> getAllFromDate(@Param("earliestDate") Date earliestDate);

    @Query("select a from Articles a where a.visible=true and a.created<=:latestDate")
    List<Articles> getAllToDate(@Param("latestDate") Date latestDate);

    @Query("select a from Articles a where a.visible=true")
    List<Articles> getAll();

    @Query("select a from Articles a where a.visible=false")
    List<Articles> getAllNotVisible();
}
