package com.banking.service;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.banking.dto.AccountCreateDto;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AccountService {
    private static final String INVALID_CREDENTIALS = "Invalid username or password";
    TransactionRepository transactionRepository;
    AccountRepository accountRepository;
    public AccountService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }
    
    
    public Account createAccount(AccountCreateDto accountCreateDto) {
        Account account = new Account();
        account.setAccountNo(accountCreateDto.getAccountNo());
        account.setUsername(accountCreateDto.getUsername());
        account.setPassword(accountCreateDto.getPassword());
        account.setAmount(accountCreateDto.getAmount());
        accountRepository.save(account);
        return account;
    }

    public Transaction deposit(String username, String password, BigDecimal amount){
        Account account = accountRepository.findByUsernameAndPassword(username, password)
            .orElseThrow(() -> new EntityNotFoundException(INVALID_CREDENTIALS));
        account.setAmount(account.getAmount().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setInitiator(account.getAccountNo());
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction withdraw(String username, String password, BigDecimal amount){
        Account account = accountRepository.findByUsernameAndPassword(username, password)
            .orElseThrow(() -> new EntityNotFoundException(INVALID_CREDENTIALS));
        account.setAmount(account.getAmount().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setInitiator(account.getAccountNo());
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction transfer(String username, String password, BigDecimal amount, Long recipientAccountNo){
        Account initiator = accountRepository.findByUsernameAndPassword(username, password)
            .orElseThrow(() -> new EntityNotFoundException(INVALID_CREDENTIALS));
        initiator.setAmount(initiator.getAmount().subtract(amount));
        accountRepository.save(initiator);

        Account recipient = accountRepository.findById(recipientAccountNo)
            .orElseThrow(() -> new EntityNotFoundException("Account with account number "+ recipientAccountNo + " does not exist"));
        initiator.setAmount(recipient.getAmount().add(amount));
        accountRepository.save(recipient);
    
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TRANSFER);
        transaction.setInitiator(initiator.getAccountNo());
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
        return transaction;
    }

    public List<Transaction> getTransactions(String username, String password){
        Account account = accountRepository.findByUsernameAndPassword(username, password)
            .orElseThrow(() -> new EntityNotFoundException(INVALID_CREDENTIALS));
        return transactionRepository.findAllByInitiator(account.getAccountNo());
    }

    public BigDecimal getBalance(String username, String password) {
        Account account = accountRepository.findByUsernameAndPassword(username, password)
            .orElseThrow(() -> new EntityNotFoundException(INVALID_CREDENTIALS));
        return account.getAmount();
    }
}
