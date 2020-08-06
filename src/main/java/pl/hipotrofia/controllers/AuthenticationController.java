package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import pl.hipotrofia.services.UserService;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService=userService;
    }

    @GetMapping("/anonymous/confirm")
    public RedirectView confirmUser(@RequestParam String token) {

        if(userService.verifyToken(token)){
            return new RedirectView("https://google.pl");
        }
        return new RedirectView("https://www.onet.pl/");
    }

}
