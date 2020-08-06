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
}
