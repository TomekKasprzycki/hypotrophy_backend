package pl.hipotrofia.services;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import pl.hipotrofia.converters.UserDtoConverter;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.entities.VerificationToken;
import pl.hipotrofia.repositories.UserRepository;
import pl.hipotrofia.validators.UserValidator;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

                String emailBody = "<p>Zakończ rejestrację klikając w poniższy link: <p><br/>" +
                        "<a href='http://localhost:8081/api/authentication/anonymous/confirm?token="
                        + verificationToken.getToken() + "' >WERYFIKACJA KONTA</a>";
                mailingService.sendMail(user.getEmail(), "Weryfikacja użytkownika", emailBody, true);
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

    public void deactivate(User user) {
        userRepository.setNotActive(user);
    }

    public void activate(User user) {
        userRepository.setActive(user);
    }

    public boolean sendVerificationToken(String email) {

        VerificationToken verificationToken;

        try {
            User user = findUserByEmail(email);
            if (verificationTokenService.getByUser(user).isPresent()) {
                verificationToken = verificationTokenService.getByUser(user).orElse(new VerificationToken());
                verificationToken.setActive(true);
                verificationToken.setExpirationDate();
                verificationToken.setToken();
            } else {
                verificationToken = verificationTokenService.createToken();
                verificationToken.setUser(user);
            }

            verificationToken = verificationTokenService.save(verificationToken);

            user.setVerificationToken(verificationToken);

            String emailBody = "<p>Otrzymaliśmy prośbę o zresetowanie hasła do Twojego konta. Aby zresetować hasło kliknij na poniższy link:</p><br/><br/>" +
                    "<a href='http://localhost:8081/api/authentication/anonymous/confirmPasswordRecovery?token="
                    + verificationToken.getToken() + "?email=" + email + "' >ZRESETUJ MOJE HASŁO</a><br/><br/>" +
                    "Jeśli prośba nie była wysłana przez Ciebie poinformuje nas o tym wysyłając mail na adres: admin@hipotrofia.info";

            mailingService.sendMail(email, "Odzyskiwanie hasła", emailBody, true);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean recoverPassword(String token, String email) {

        String newPassword;

        try {
            User user = findUserByEmail(email);
            VerificationToken verificationToken = verificationTokenService.getByUser(user).orElseThrow(Exception::new);

            if (token.equals(verificationToken.getToken())) {

                newPassword = createPassword();
                String emailBody = "<p>Twoje nowe hasło, to: " + newPassword + "</p><br/><br/>";
                mailingService.sendMail(email, "Odzyskiwanie hasła", emailBody, true);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    protected String createPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 38, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public boolean changePassword(UserDto userDto) {

        try {
            if(userValidator.isTheUserValid(userDto)) {
                User user = findUserByEmail(userDto.getEmail());
                user.setPassword(userDto.getPassword());
                save(user);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
