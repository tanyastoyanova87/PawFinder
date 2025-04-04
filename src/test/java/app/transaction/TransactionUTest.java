package app.transaction;

import app.transaction.model.Transaction;
import app.transaction.model.TransactionStatus;
import app.transaction.repository.TransactionRepository;
import app.transaction.service.TransactionService;
import app.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionUTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void givenParameters_whenCreateNewTransaction_returnAndSaveTransaction() {
        User user = User.builder().build();
        String sender = "Sender";
        String receiver = "Receiver";
        BigDecimal amount = BigDecimal.valueOf(0);
        BigDecimal balanceLeft = BigDecimal.valueOf(0);
        TransactionStatus status = TransactionStatus.SUCCEEDED;
        String description = "idk";
        String failure = "idk";

        transactionService.createNewTransaction(user, sender, receiver, amount, balanceLeft, status, description, failure);

        assertThat(transactionRepository).isNotNull();
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void givenUserId_whenFindAllTransactionsByUserId_returnTransactionsByUserId() {
        UUID userId = UUID.randomUUID();

        List<Transaction> transactions = transactionService.findAllTransactionsByUserId(userId);

        assertThat(transactions).isNotNull();
    }
}
