package pl.hipotrofia.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.hipotrofia.services.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final TokenService tokenService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  TokenService tokenService) {
        super(authenticationManager);
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        if (header != null) {
            Optional<UsernamePasswordAuthenticationToken> authResult = getAuthentication(header.replace("Bearer ", ""));

            authResult.ifPresent(usernamePasswordAuthenticationToken -> SecurityContextHolder
                    .getContext()
                    .setAuthentication(usernamePasswordAuthenticationToken));
        }


        chain.doFilter(request, response);
    }

    private Optional<UsernamePasswordAuthenticationToken> getAuthentication(String header) {

        //Token musi być Optional
        if (!tokenService.findByToken(header).isActive()) {
            return Optional.empty();
        }


        final Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET.getBytes())
                .parseClaimsJws(header);

        String email = claimsJws.getBody().get("email").toString();
        String role = claimsJws.getBody().get("role").toString();

        final Set<SimpleGrantedAuthority> simpleGrantedAuthority = Collections.singleton(new SimpleGrantedAuthority(role));

        return Optional.of(new UsernamePasswordAuthenticationToken(email, null, simpleGrantedAuthority));
    }
}
