package app.sheduled;

import app.adoption.model.Adoption;
import app.adoption.service.AdoptionService;
import app.email.service.EmailService;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReminderPendingAdoptions {
    private final UserService userService;
    private final AdoptionService adoptionService;
    private final EmailService emailService;

    public ReminderPendingAdoptions(UserService userService, AdoptionService adoptionService, EmailService emailService) {
        this.userService = userService;
        this.adoptionService = adoptionService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 30 12 * * ?")
    public void sendAdoptionRequestReminders() {
        User admin = userService.getAdmin();
        List<Adoption> pendingAdoptions = adoptionService.getAllPendingAdoptions();

        String subjectEmail = "Pending adoptions";
        String body = "Hey, %s!%nYou have %d adoption requests to review today.%nThe PawFinder Team".formatted(admin.getFirstName(), pendingAdoptions.size());
        String sender = "pawfinder2025@gmail.com";
        if (!pendingAdoptions.isEmpty()) {
            emailService.sendEmail(admin.getId(), subjectEmail, body, admin.getEmail(), sender);
        }
    }
}
