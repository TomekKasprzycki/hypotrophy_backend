package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.entities.VerificationToken;
import pl.hipotrofia.repositories.VerificationTokenRepository;

import java.util.Optional;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;


    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository=verificationTokenRepository;
    }

    public VerificationToken createToken() {


        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken();
        verificationToken.setActive(true);
        verificationToken.setExpirationDate();

        return verificationToken;
    }

    public VerificationToken save(VerificationToken verificationToken) {
        return verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken getByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void remove(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }

    public Optional<VerificationToken> getByUser(User user) {
        return verificationTokenRepository.findByUser(user);
    }
}
