package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.converters.UserDtoConverter;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.entities.VerificationToken;
import pl.hipotrofia.repositories.UserRepository;
import pl.hipotrofia.validators.UserValidator;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserDtoConverter userDtoConverter;
    private final RoleService roleService;
    private final VerificationTokenService verificationTokenService;
    private final MailingService mailingService;

    public UserService(UserRepository userRepository,
                       UserValidator userValidator,
                       UserDtoConverter userDtoConverter,
                       RoleService roleService,
                       VerificationTokenService verificationTokenService,
                       MailingService mailingService) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userDtoConverter = userDtoConverter;
        this.roleService = roleService;
        this.verificationTokenService = verificationTokenService;
        this.mailingService = mailingService;
    }

    public boolean registerUser(UserDto userDto) {

        long millis = System.currentTimeMillis();
        if (userValidator.isTheUserValid(userDto)) {
            User user = userDtoConverter.convertFromDto(userDto);
            user.setCreated(new Date(millis));
            user.setRole(roleService.getRole(3L));

            VerificationToken verificationToken = verificationTokenService.createToken();

            user.setVerificationToken(verificationToken);

            try {
                verificationToken = verificationTokenService.save(verificationToken);
                user = userRepository.save(user);
                verificationToken.setUser(user);
                verificationTokenService.save(verificationToken);



                String url = "<p>Zakończ rejestrację klikając w poniższy link: <p><br/>" +
                        "<a href='http://localhost:8081/api/authentication/anonymous/confirm?token=" + verificationToken.getToken() + "' >WERYFIKACJA KONTA</a>";
                mailingService.sendMail(user.getEmail(), "Weryfikacja użytkownika", url, true);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }

        } else {
            return false;
        }
    }

    public boolean verifyToken(String token) {

        long millis = System.currentTimeMillis();
        Date now = new Date(millis);
        boolean result = false;

        try {
            VerificationToken verificationToken = verificationTokenService.getByToken(token);
            final User user = verificationToken.getUser();
            if (now.before(user.getVerificationToken().getExpirationDate()) && verificationToken.isActive()) {
                user.setActive(true);
                save(user);
                verificationToken.setActive(false);
                verificationTokenService.save(verificationToken);
                result = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private User findUserByVerificationToken(VerificationToken verificationToken) {
        return userRepository.findByVerificationToken(verificationToken);
    }

    public User findUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User findUserById(Long parent) {
        return userRepository.getUserById(parent);
    }

    public List<User> getAllLimited(int limit, int offset) {
        return userRepository.findAllLimited(limit, offset);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
