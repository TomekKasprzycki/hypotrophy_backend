package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.User;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDtoConverter {

    public UserDto convertToDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoleName(user.getRole().getName());
        userDto.setActive(user.isActive());

        return userDto;
    }

    public User convertFromDto(UserDto userDto) {

        long milis = System.currentTimeMillis();
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPasswordChange(new Date(milis));
        user.setActive(userDto.isActive());

        return user;
    }

    public List<UserDto> convertToDto(List<User> users) {
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
