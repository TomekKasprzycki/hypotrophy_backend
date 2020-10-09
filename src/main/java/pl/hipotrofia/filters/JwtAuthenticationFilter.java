package pl.hipotrofia.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.Token;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.services.TokenService;
import pl.hipotrofia.services.UserService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokenService tokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   TokenService tokenService,
                                   UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String header = request.getHeader("TYPE");
        if (header == null) {
            return null;
        }
        UsernamePasswordAuthenticationToken authenticationToken = null;
        UserDto userDto = parseUserDto(request);

        switch (header) {
            case "Registration":

                if (userService.registerUser(userDto)) {
                    response.setHeader("Status", "REGISTERED");
                } else {
                    response.setHeader("Status", "DENIED");
                }
                break;

            case "Login":

                try {
                    User user = userService.findUserByEmail(userDto.getEmail());
                    if (user.isActive() && user.checkPassword(userDto.getPassword())) {
                        authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            case "Logout":
                String tokenToDeactivation = request.getHeader("Authorization").replace("Bearer ", "");
                try {
                    Token token = tokenService.findByToken(tokenToDeactivation);
                    token.setActive(false);
                    tokenService.addToken(token);

                    response.setHeader("Status", "LOGOUT");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    response.setHeader("Status", "DENIED");
                }
                break;
        }

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {


        final org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User user = userService.findUserByEmail(principal.getUsername());
        String token = tokenService.createToken(user);


        try {
            Token createdToken = tokenService.findByUser(user);
            if (createdToken == null) {
                createdToken = new Token();
                createdToken.setToken(token);
                createdToken.setActive(true);
                createdToken.setUser(user);
                createdToken = tokenService.addToken(createdToken);
                user.setToken(createdToken);
                userService.save(user);
            } else {
                createdToken.setToken(token);
                createdToken.setActive(true);
            }
            tokenService.addToken(createdToken);


        } catch (Exception ex) {
            ex.printStackTrace();
            response.setHeader("ERROR", ex.getMessage());
        }
        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);

    }

    private UserDto parseUserDto(HttpServletRequest request) {
        if (request.getMethod().equalsIgnoreCase("post")) {
            try {
                final String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                return objectMapper.readValue(requestBody, UserDto.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new UserDto();
    }

}
