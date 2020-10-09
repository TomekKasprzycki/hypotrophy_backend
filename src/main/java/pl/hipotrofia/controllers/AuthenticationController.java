package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.hipotrofia.entities.Token;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.filters.SecurityConstants;
import pl.hipotrofia.services.TokenService;
import pl.hipotrofia.services.UserService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final TokenService tokenService;
    private final UserService userService;

    public AuthenticationController(TokenService tokenService,
                                    UserService userService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @GetMapping("/refresh")
    public void refresh(HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findUserByEmail(userName);

        try {
            final String token = tokenService.createToken(user);
            Token createdToken = tokenService.findByUser(user);
            createdToken.setToken(token);
            tokenService.addToken(createdToken);
            response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        } catch (Exception ex) {
            response.setHeader("ERROR", ex.getMessage());
        }

    }

    @GetMapping("/anonymous/confirm")
    public void confirmUser(@RequestParam String token, HttpServletResponse response) {
        if (userService.verifyToken(token)) {
            response.setHeader("STATUS", "OK");
        } else {
            response.setHeader("STATUS", "DENIED");
        }
    }

}
