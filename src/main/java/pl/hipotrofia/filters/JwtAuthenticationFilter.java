package pl.hipotrofia.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hibernate.HibernateException;
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
import java.util.Date;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokenService tokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   UserService userService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String header = request.getHeader("TYPE");
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

                User user = userService.findUserByEmail(userDto.getEmail());
                authenticationToken = user != null ?
                        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()) : null;
                break;

            case "Refresh":
                //TODO
                break;
        }

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {

        UserDto userDto = ((UserDto) authentication.getPrincipal());
        User user = userService.findUserByEmail(userDto.getEmail());
        long currentDateMiliseconds = System.currentTimeMillis();
        long expirationTime = 20 * 60 * 1000; //20 minutes

        byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

        if (user.checkPassword(userDto.getPassword())) {
            String token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .claim("name", user.getName())
                    .claim("email", user.getEmail())
                    .claim("role", user.getRole().getId())
                    .setIssuedAt(new Date(currentDateMiliseconds))
                    .setExpiration(new Date(currentDateMiliseconds + expirationTime))
                    .signWith(SignatureAlgorithm.HS256, signingKey)
                    .compact();

            try {
                Token createdToken = new Token();
                createdToken.setToken(token);
                createdToken.setActive(true);
                createdToken.setUser(user);
                tokenService.addToken(createdToken);
            } catch (HibernateException ex) {
                ex.printStackTrace();
            }
            response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
            
        }
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
