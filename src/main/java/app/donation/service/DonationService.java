package app.donation.service;

import app.creditCard.model.CreditCard;
import app.donation.model.Donation;
import app.donation.repository.DonationRepository;
import app.email.service.EmailService;
import app.exception.DomainException;
import app.transaction.model.TransactionStatus;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.web.dto.DonationRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final TransactionService transactionService;
    private final EmailService emailService;

    public DonationService(DonationRepository donationRepository, TransactionService transactionService, EmailService emailService) {
        this.donationRepository = donationRepository;
        this.transactionService = transactionService;
        this.emailService = emailService;
    }

    public void donateAmount(User user, DonationRequest donationRequest) {
        String receiver = "PawFinder";

        CreditCard creditCard = user.getCreditCard();
        if (creditCard.getBalance().compareTo(donationRequest.getAmount()) < 0) {

            String description = "Donation for %s from %s to PawFinder.".formatted(donationRequest.getAmount(), user.getId());
            String failureReason = "Not enough money for donation.";
            transactionService.createNewTransaction(user, user.getFirstName() + " " + user.getLastName(),
                    receiver, donationRequest.getAmount(), creditCard.getBalance(), TransactionStatus.FAILED,
                    description, failureReason);

            throw new DomainException("You do not have enough money for this amount of donation.");
        }

        creditCard.setBalance(creditCard.getBalance().subtract(donationRequest.getAmount()));

        Donation donation = Donation.builder()
                .owner(user)
                .amount(donationRequest.getAmount())
                .creditCard(user.getCreditCard())
                .createdOn(LocalDateTime.now())
                .build();

        donationRepository.save(donation);

        String description = "Donation for %s from %s to PawFinder.".formatted(donationRequest.getAmount(), user.getId());
        transactionService.createNewTransaction(user, user.getFirstName() + " " + user.getLastName(),
                receiver, donationRequest.getAmount(), creditCard.getBalance(), TransactionStatus.SUCCEEDED,
                description, null);

        String subject = "Successful donation";
        String body = "Hey, %s!%nThank you for your donation! Because of your support, we can provide food, medical care, and shelter to animals in need, giving them the love and attention they deserve.%nBest regards,%nThe PawFinder Team".formatted(user.getFirstName());
        String emailsSender = "pawfinder2025@gmail.com";
        emailService.sendEmail(user.getId(), subject, body, user.getEmail(), emailsSender);
    }

    public List<Donation> findAll() {
        return donationRepository.findAll();
    }
}
