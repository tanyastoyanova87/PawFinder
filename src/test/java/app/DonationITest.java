package app;

import app.creditCard.model.CreditCard;
import app.creditCard.repository.CreditCardRepository;
import app.donation.model.Donation;
import app.donation.repository.DonationRepository;
import app.donation.service.DonationService;
import app.exception.InsufficientFundsException;
import app.transaction.model.Transaction;
import app.transaction.model.TransactionStatus;
import app.transaction.repository.TransactionRepository;
import app.user.model.Country;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.DonationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DonationITest {

    @Autowired
    private DonationService donationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void givenUserAndDonationRequest_makeDonation() {
        User user = User.builder()
                .username("Someone")
                .firstName("Tosho")
                .lastName("Toshkov")
                .password("12345sksk")
                .confirmPassword("12345sksk")
                .email("tosho@abv.bg")
                .role(UserRole.USER)
                .country(Country.BULGARIA)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        userRepository.save(user);

        CreditCard creditCard = CreditCard.builder()
                .owner(user)
                .cardHolderName("Tosho Toshkov")
                .cardCVV("123")
                .cardNumber("1234567891234567")
                .cardExpiration(LocalDateTime.now().plusYears(2))
                .balance(new BigDecimal(100))
                .build();

        user.setCreditCard(creditCard);
        creditCardRepository.save(creditCard);


        DonationRequest donationRequest = DonationRequest.builder()
                .amount(new BigDecimal(5))
                .build();

        donationService.donateAmount(user, donationRequest);
        List<Donation> donations = donationService.findAll();

        assertThat(donations.size()).isEqualTo(1);
        assertThat(creditCard.getBalance()).isEqualTo(new BigDecimal(95));
        assertThat(transactionRepository.findAll().size()).isEqualTo(1);
        assertThat(donationRepository.findAll().size()).isEqualTo(1);

        Optional<Transaction> transaction = transactionRepository.findAll().stream().filter(t -> t.getStatus().equals(TransactionStatus.SUCCEEDED)).findFirst();
        assertTrue(transaction.isPresent());
        Transaction transaction1 = transaction.get();
        assertThat(transaction1.getStatus()).isEqualTo(TransactionStatus.SUCCEEDED);
    }

    @Test
    void givenUserAndDonationRequest_whenDonateCheckBalanceAndThrowExceptionWhenNotEnoughMoney() {
        User user = User.builder()
                .username("Someone")
                .firstName("Tosho")
                .lastName("Toshkov")
                .password("12345sksk")
                .confirmPassword("12345sksk")
                .email("tosho@abv.bg")
                .role(UserRole.USER)
                .country(Country.BULGARIA)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        userRepository.save(user);

        CreditCard creditCard = CreditCard.builder()
                .owner(user)
                .cardHolderName("Tosho Toshkov")
                .cardCVV("123")
                .cardNumber("1234567891234567")
                .cardExpiration(LocalDateTime.now().plusYears(2))
                .balance(new BigDecimal(100))
                .build();

        user.setCreditCard(creditCard);
        creditCardRepository.save(creditCard);


        DonationRequest donationRequest = DonationRequest.builder()
                .amount(new BigDecimal(105))
                .build();

        assertThrows(InsufficientFundsException.class, () -> donationService.donateAmount(user, donationRequest));
    }
}
