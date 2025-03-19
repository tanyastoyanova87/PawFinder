package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {
//    private final EmailSenderService senderService;
//
//    public Application(EmailSenderService senderService) {
//        this.senderService = senderService;
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void sendMail() {
//        senderService.sendEmail("test123test123test123321321321@gmail.com",
//                "This is subject", "This is body of email");
//    }
}


