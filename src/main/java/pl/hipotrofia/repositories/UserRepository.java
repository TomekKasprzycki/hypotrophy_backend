package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.entities.VerificationToken;

import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email=:email")
    User getUserByEmail(@Param("email") String email);

    @Query("select u from User u where u.id=:parent")
    User getUserById(@Param("parent") Long parent);

    @Query(nativeQuery = true, value = "select * from hypotrophy.user limit :limit offset :offset")
    List<User> findAllLimited(@Param("limit") int limit,@Param("offset") int offset);

    @Query("select u from User u where u.token=:id")
    User findByVerificationToken(@Param("id") VerificationToken id);

    @Query("update User u set u.active=false where u=:user")
    void setNotActive(User user);

    @Query("update User u set u.active=true where u=:user")
    void setActive(User user);

    @Query("select count(u) from User u where u.name=:name")
    int isUserNameTaken(String name);

}
