package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.User;

@Service
public class UserDtoConverter {

    public UserDto convertToDto(User user){

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());


        return userDto;
    }

}
