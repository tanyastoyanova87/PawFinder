package app.transaction.service;

import app.transaction.model.Transaction;
import app.transaction.model.TransactionStatus;
import app.transaction.repository.TransactionRepository;
import app.user.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void createNewTransaction(User owner, String sender, String receiver, BigDecimal amount, BigDecimal balanceLeft, TransactionStatus status, String description, String failureReason) {
        Transaction transaction = Transaction.builder()
                .owner(owner)
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .balanceLeft(balanceLeft)
                .status(status)
                .description(description)
                .failureReason(failureReason)
                .createdOn(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }

    public List<Transaction> findAllTransactionsByUserId(UUID id) {
        return transactionRepository.findAllByOwnerIdOrderByCreatedOnDesc(id);
    }
}
