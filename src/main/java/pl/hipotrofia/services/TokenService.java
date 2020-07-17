package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Token;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.repositories.TokenRepository;

@Service
public class TokenService {

    private TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository=tokenRepository;
    }

    public Token addToken(Token token) {
        return tokenRepository.save(token);
    }

    public Token findByUser(User user) {
        return tokenRepository.getByUser(user);
    }
}
