package pl.hipotrofia.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.services.UserService;

import java.util.Date;

@RestController
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")

    public String loginUser(@RequestBody UserDto userDto){

        long currentDateMiliseconds = System.currentTimeMillis();
        long expirationTime = 20 * 60 * 1000; //20 minutes
        String response = "Denied";

        User user = userService.findUserByEmail(userDto.getEmail());

        if (user.checkPassword(userDto.getPassword())){
            response = Jwts.builder()
                    .setSubject(userDto.getEmail())
                    .claim("name",user.getName())
                    .claim("email", user.getEmail())
                    .claim("role", user.getRole().getId())
                    .setIssuedAt(new Date(currentDateMiliseconds))
                    .setExpiration(new Date(currentDateMiliseconds + expirationTime))
                    .signWith(SignatureAlgorithm.HS256, userDto.getPassword())
                    .compact();
        }


        return response;
    }

}
