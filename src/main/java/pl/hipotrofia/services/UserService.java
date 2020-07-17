package pl.hipotrofia.services;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import pl.hipotrofia.converters.UserDtoConverter;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.repositories.UserRepository;
import pl.hipotrofia.validators.UserValidator;

import java.util.Date;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserDtoConverter userDtoConverter;
    private final RoleService roleService;

    public UserService(UserRepository userRepository,
                       UserValidator userValidator,
                       UserDtoConverter userDtoConverter,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userDtoConverter = userDtoConverter;
        this.roleService = roleService;
    }

    public boolean registerUser(UserDto userDto) throws HibernateException {

        long milis = System.currentTimeMillis();
        if (userValidator.isTheUserValid(userDto)) {
            User user = userDtoConverter.convertFromDto(userDto);
            user.setCreated(new Date(milis));
            user.setRole(roleService.getRole(1L));
            try {
                userRepository.save(user);
                return true;
            } catch (HibernateException ex) {
                ex.printStackTrace();
                return false;
            }

        } else {
            return false;
        }
    }

    public User findUserByEmail(String email){ return userRepository.getUserByEmail(email); }

    public User findUserById(Long parent) {
        return userRepository.getUserById(parent);
    }
}
