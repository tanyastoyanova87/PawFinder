package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String toEmail, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("taniastoianova3002@gmail.com");
//        message.setTo(toEmail);
//        message.setSubject(subject);
//        message.setText(body);
//
//        javaMailSender.send(message);
//
//        System.out.println("mail sent successfully.......");
    }
}
