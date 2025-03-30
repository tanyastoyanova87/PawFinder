package app.creditCard.service;

import app.creditCard.model.CreditCard;
import app.creditCard.repository.CreditCardRepository;
import app.user.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;

    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public CreditCard generateCreditCard(User user) {
        CreditCard creditCard = CreditCard.builder()
                .owner(user)
                .cardHolderName(user.getFirstName() + " " + user.getLastName())
                .cardNumber(generateRandomCreditCardNumber(16))
                .cardCVV(generateRandomCreditCardNumber(3))
                .cardExpiration(LocalDateTime.now().plusYears(2))
                .balance(BigDecimal.valueOf(200))
                .build();

        creditCardRepository.save(creditCard);
        return creditCard;
    }

    private String generateRandomCreditCardNumber(int numbersCount) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numbersCount; i++) {
            builder.append(random.nextInt(0, 9));
        }

        return builder.toString();
    }
}
