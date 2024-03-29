package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Role;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleById(Long id);

    @Query("select r from Role r where r.name=:roleName")
    Role getRoleByName(@Param("roleName") String roleName);
}
