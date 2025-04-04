package app.creditCard;

import app.creditCard.model.CreditCard;
import app.creditCard.repository.CreditCardRepository;
import app.creditCard.service.CreditCardService;
import app.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditCardUTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardService creditCardService;

    @Test
    void givenUser_whenCreateNewCreditCard_thenCreateAndSaveCreditCardAndSetItToUser() {
        User user = User.builder()
                .build();

        CreditCard newCreditCard = creditCardService.createNewCreditCard(user);

        assertThat(newCreditCard).isNotNull();
        assertThat(newCreditCard.getOwner()).isEqualTo(user);
        verify(creditCardRepository, times(1)).save(any(CreditCard.class));
    }
}
