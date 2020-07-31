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
        if (header==null) {
            header = "WithoutToken";
        }

        UsernamePasswordAuthenticationToken authResult = getAuthentication(header.replace("Bearer ",""));

        SecurityContextHolder.getContext().setAuthentication(authResult);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String header) {

        try {
            if (!tokenService.findByToken(header).isActive()) {
                return null;
            }
        } catch (NullPointerException ex){
            ex.printStackTrace();
            return null;
        }

        final Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET.getBytes())
                .parseClaimsJws(header);

        String email = claimsJws.getBody().get("email").toString();
        String role = claimsJws.getBody().get("role").toString();

        final Set<SimpleGrantedAuthority> simpleGrantedAuthority = Collections.singleton(new SimpleGrantedAuthority(role));

        return new UsernamePasswordAuthenticationToken(email, null, simpleGrantedAuthority);
    }
}
