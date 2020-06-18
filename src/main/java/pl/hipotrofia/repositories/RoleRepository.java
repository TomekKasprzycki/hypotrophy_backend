package pl.hipotrofia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.hipotrofia.entities.Role;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleById(Long id);

}
