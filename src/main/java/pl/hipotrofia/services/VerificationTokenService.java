package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.VerificationToken;
import pl.hipotrofia.repositories.VerificationTokenRepository;

import java.util.Date;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;


    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository=verificationTokenRepository;
    }

    public VerificationToken createToken() {
        long millis = System.currentTimeMillis();
        long expirationDate = millis + 24 * 60 * 60 * 1000;

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken();
        verificationToken.setActive(true);
        verificationToken.setExpirationDate(new Date(expirationDate));

        return verificationToken;
    }

    public VerificationToken save(VerificationToken verificationToken) {
        return verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken getByToken(String token) {
        return verificationTokenRepository.findAllByVerificationToken(token);
    }

    public void remove(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }
}
