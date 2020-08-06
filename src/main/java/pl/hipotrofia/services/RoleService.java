package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Role;
import pl.hipotrofia.repositories.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    Role getRole(Long id) {
        return roleRepository.getRoleById(id);
    }

    public Role findByName(String roleName) {
        return roleRepository.getRoleByName(roleName);
    }
}
