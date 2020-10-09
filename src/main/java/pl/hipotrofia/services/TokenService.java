package pl.hipotrofia.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Token;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.filters.SecurityConstants;
import pl.hipotrofia.repositories.TokenRepository;

import java.util.Date;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository=tokenRepository;
    }

    public Token addToken(Token token) {
        return tokenRepository.save(token);
    }

    public Token findByUser(User user) {
        return tokenRepository.getByUser(user);
    }

    public Token findByToken(String tokenToDeactivation) {
        return tokenRepository.getByToken(tokenToDeactivation);
    }

    public String createToken(User user){

        long currentDateMilliseconds = System.currentTimeMillis();
        long expirationTime = 20 * 60 * 1000; //20 minutes

        byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().getName())
                .setIssuedAt(new Date(currentDateMilliseconds))
                .setExpiration(new Date(currentDateMilliseconds + expirationTime))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

}
