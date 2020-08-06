package pl.hipotrofia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.hipotrofia.services.MailingService;

import javax.mail.MessagingException;

@RestController
public class MailingController {

    private final MailingService mailService;

    @Autowired
    public MailingController(MailingService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/api/mail/anonymous/sendMail")
    public String sendMail() throws MessagingException {
        mailService.sendMail("tomasz.kasprzycki@gmail.com",
                "Test mailingu",
                "<b>1000 000 zł</b><br>:P", true);
        return "wysłano";
    }
}
