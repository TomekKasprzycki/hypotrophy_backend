package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.Token;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.filters.SecurityConstants;
import pl.hipotrofia.myExceptions.UserNotFoundException;
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
    public void refresh(HttpServletResponse response) throws UserNotFoundException {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findUserByEmail(userName).orElseThrow(() -> new UserNotFoundException("User not found"));

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
    public RedirectView confirmUser(@RequestParam String token, HttpServletResponse response) {

        if (userService.verifyToken(token)) {
            response.setHeader("STATUS", "OK");
            return new RedirectView("http://localhost:3000/login");
        } else {
            response.setHeader("STATUS", "DENIED");
            return new RedirectView("http://localhost:3000/"); //maybe we should have a page which inform user about that error
        }
    }

    @GetMapping("/anonymous/passwordRecovery")
    public RedirectView passwordRecovery(@RequestParam String email, HttpServletResponse response) {

        if (userService.sendVerificationToken(email)) {
            return new RedirectView("/success");
        } else {
            return new RedirectView("/error");
        }
    }

    @GetMapping("/anonymous/confirmPasswordRecovery")
    public RedirectView confirmPasswordRecovery(@RequestParam String token, @RequestParam String email) {

        if (userService.recoverPassword(token, email)) {
            return new RedirectView("/success");
        } else {
            return new RedirectView("/error");
        }
    }

    @PostMapping("/passwordChange")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    public void passwordChange(@RequestBody UserDto userDto, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (userName.equals(userDto.getEmail())) {
            if (userService.changePassword(userDto)) {
                response.setHeader("TYPE", "Success");
            } else {
                response.setHeader("TYPE", "Declined");
            }
        } else {
            response.setStatus(404);
            response.setHeader("ERROR", "Something went wrong...");
        }
    }



}
