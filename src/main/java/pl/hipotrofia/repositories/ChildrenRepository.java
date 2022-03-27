package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Children;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ChildrenRepository extends JpaRepository<Children, Long> {

    @Query("select c from Children c where c.user.id=:parentId")
    Optional<List<Children>> findAllByParentId(@Param("parentId") Long parentId);

    @Query("select ch from Children ch join fetch User where ch.id=:kidId and ch.user.email=:userName")
    Children findChildByParentAndId(@Param("userName") String userName,@Param("kidId") Long kidId);

}
