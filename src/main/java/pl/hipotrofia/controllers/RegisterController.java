package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.services.UserService;

@RestController
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/registration")
    public boolean registerUser(UserDto userDto) {
        return userService.registerUser(userDto);
    }


}
