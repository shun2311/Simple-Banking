package com.banking.service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.banking.dto.AccountActionDto;
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
    
    
    public Account createAccount(AccountActionDto accountCreateDto) {
        // Must ensure user start with at least 0 in balance
        if(accountCreateDto.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot start with negative balance");
        }

        // Minimum 6 characters 
        if(accountCreateDto.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be greater than 6 characters");
        }

        Optional<Account> accountOpt = accountRepository.findByUsername(accountCreateDto.getUsername());

        // Cannot create account with same user name
        if(accountOpt.isPresent()) {
            throw new IllegalArgumentException("Account with the same username already exist");
        }
        
        Account account = new Account();
        account.setUsername(accountCreateDto.getUsername());
        // Should be using keycloak instead
        account.setPassword(accountCreateDto.getPassword());
        account.setAmount(accountCreateDto.getAmount());
        accountRepository.save(account);
        return account;
    }

    // Deposit minimum: 1
    public Transaction deposit(String username, String password, BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cannot deposit negative or zero amount");
        }

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

    // Withdraw minimum: 1
    // Withdraw maximum: Available Balance
    public Transaction withdraw(String username, String password, BigDecimal amount){
        Account account = accountRepository.findByUsernameAndPassword(username, password)
            .orElseThrow(() -> new EntityNotFoundException(INVALID_CREDENTIALS));
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cannot withdraw negative or zero amount");
        }
        if(amount.compareTo(account.getAmount()) > 0) {
            throw new IllegalArgumentException("Cannot withdraw more than available balance");
        }
        
        account.setAmount(account.getAmount().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setInitiator(account.getAccountNo());
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
        return transaction;
    }

    // Transfer minimum: 1
    // Transfer maximum: Available Balance
    public Transaction transfer(String username, String password, BigDecimal amount, Long recipientAccountNo){
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cannot transfer negative or zero amount");
        }
       
        Account initiator = accountRepository.findByUsernameAndPassword(username, password)
            .orElseThrow(() -> new EntityNotFoundException(INVALID_CREDENTIALS));

        if (amount.compareTo(initiator.getAmount()) > 0) {
            throw new IllegalArgumentException("Cannot transfer more than available balance");
        }
        if (initiator.getAccountNo().equals(recipientAccountNo)) {
            throw new IllegalArgumentException("Cannot transfer to own account");
        }
        Account recipient = accountRepository.findById(recipientAccountNo)
            .orElseThrow(() -> new EntityNotFoundException("Account with account number "+ recipientAccountNo + " does not exist"));

        
        initiator.setAmount(initiator.getAmount().subtract(amount));
        accountRepository.save(initiator);

        recipient.setAmount(recipient.getAmount().add(amount));
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
