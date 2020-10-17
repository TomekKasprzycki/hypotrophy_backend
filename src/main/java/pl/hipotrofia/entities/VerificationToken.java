package pl.hipotrofia.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    private Date expirationDate;
    private boolean active;
    @OneToOne(mappedBy = "verificationToken",fetch = FetchType.EAGER)
    private User user;

    public void setToken() {
        this.token = UUID.randomUUID().toString();
    }

    public void setExpirationDate() {
        long millis = System.currentTimeMillis();
        long expirationDateMillis = millis + 24 * 60 * 60 * 1000;
        this.expirationDate = new Date(expirationDateMillis);}
}
