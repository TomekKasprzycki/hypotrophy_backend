package pl.hipotrofia.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service
public class MailingService {

    private final JavaMailSender javaMailSender;
    private final MailingListService mailingListService;

    public MailingService(JavaMailSender javaMailSender,
                          MailingListService mailingListService) {
        this.javaMailSender = javaMailSender;
        this.mailingListService=mailingListService;
    }


    protected void sendMail(String to,
                         String subject,
                         String text,
                         boolean isHtmlContent) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessage.setFrom("hipotrofia.test@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, isHtmlContent);
        javaMailSender.send(mimeMessage);
    }

    public void sendEmailToAdmin(HttpServletResponse response, String subject, String contents) {

        Map<String, String> adminEmailList = mailingListService.getMailingList(1L);

        adminEmailList.keySet().forEach(email -> {
            try {
                sendMail(email, subject,
                        adminEmailList.get(email) + "!<br/><br/>"
                                + contents + "!<br/><br/>"
                                + "Pozdrawiam," + "<br/>>"
                                + "Backend",
                        false);
                response.setHeader("MAILING", "Success");
            } catch (MessagingException e) {
                e.printStackTrace();
                response.setHeader("MAILING", e.getCause().getMessage());
            }
        });
    }

    public void sendEmailToUsers(HttpServletResponse response, String subject, String contents) {

        Map<String, String> usersEmailList = mailingListService.getMailingList(3L);

        usersEmailList.keySet().forEach(email -> {
            try {
                sendMail(email, subject,
                        usersEmailList.get(email) + "!<br/><br/>"
                                + contents + "<br/><br/>"
                                + "Pozdrawiamy," + "<br/>"
                                + "Zespół hipotrofia.info",
                        false);
                response.setHeader("MAILING", "Success");
            } catch (MessagingException e) {
                e.printStackTrace();
                response.setHeader("MAILING", e.getCause().getMessage());
            }
        });
    }


}