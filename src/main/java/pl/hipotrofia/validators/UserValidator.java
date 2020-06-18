package pl.hipotrofia.validators;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.UserDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidator {

    //password must have at least 8 characters, max 16 characters, must have at last one:
    // capital letter, small letter, digit and special character - !@#$%^&*()
    private boolean isPasswordValid(String pass1, String pass2){
        final Pattern pattern = Pattern.compile("^([!@#$%^&*()a-zA-Z0-9]*\\d*[A-Z]+\\d*[!@#$%^&*()a-zA-Z0-9]*\\d*)+$");
        final Matcher matcher = pattern.matcher(pass1);
        final boolean condition1 = pass1.equals(pass2);
        final boolean condition2 = matcher.matches();
        final boolean condition3 = pass1.length()>7 && pass1.length()<17;
        return condition1 && condition2 && condition3;
    }

    private boolean isEmailCorrect(String email){
        final Pattern pattern = Pattern.compile("^([a-zA-Z0-9_]+)(\\.[a-zA-Z0-9_]+)*(@)([a-zA-Z0-9]+)(\\-{1}[a-zA-Z0-9])*\\.[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*$");
        final Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public boolean isTheUserValid(UserDto userDto){

        final boolean condition1 = !userDto.getName().isEmpty();
        final boolean condition2 = isPasswordValid(userDto.getPassword(), userDto.getPassword2());
        final boolean condition3 = isEmailCorrect(userDto.getEmail());

        return condition1 && condition2 && condition3;
    }

}
