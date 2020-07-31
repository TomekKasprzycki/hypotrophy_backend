package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Token;
import pl.hipotrofia.entities.User;

@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t from Token t where t.user=:user")
    Token getByUser(@Param("user") User user);

    @Query("select t from Token t where t.token=:tokenToDeactivation")
    Token getByToken(@Param("tokenToDeactivation") String tokenToDeactivation);
}
