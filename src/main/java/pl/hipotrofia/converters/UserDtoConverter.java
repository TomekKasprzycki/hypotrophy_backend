package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.User;

import java.time.LocalDate;

@Service
public class UserDtoConverter {

    public UserDto convertToDto(User user) {

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());


        return userDto;
    }

    public User convertFromDto(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPasswordChange(LocalDate.now());

        return user;
    }
}
