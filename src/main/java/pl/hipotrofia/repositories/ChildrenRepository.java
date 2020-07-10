package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Children;

import java.util.List;

@Repository
@Transactional
public interface ChildrenRepository extends JpaRepository<Children, Long> {

    @Query("select c from Children c where c.user.id=:parentId")
    List<Children> findAllByParentId(@Param("parentId") Long parentId);
}
