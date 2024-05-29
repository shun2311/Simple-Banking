package com.banking.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.dto.AccountActionDto;
import com.banking.dto.AccountCreateDto;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.service.AccountService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/account")
public class AccountController {
    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("")
    public ResponseEntity<Account> createAccount(
            @RequestBody AccountCreateDto accountCreateDto) {
        return new ResponseEntity<>(accountService.createAccount(accountCreateDto), HttpStatus.CREATED);
    }  

    @PutMapping(value = "/deposit")
    public ResponseEntity<Transaction> deposit(
        @RequestBody AccountActionDto accountActionDto) {
            return new ResponseEntity<>(
                accountService.deposit(accountActionDto.getUsername(), accountActionDto.getPassword(),accountActionDto.getAmount()), 
                HttpStatus.OK);
    }

    @PutMapping(value = "/withdraw")
    public ResponseEntity<Transaction> withdraw(
        @RequestBody AccountActionDto accountActionDto) {
            return new ResponseEntity<>(
                accountService.withdraw(accountActionDto.getUsername(), accountActionDto.getPassword(), accountActionDto.getAmount()), 
                HttpStatus.OK);
    }

    @PutMapping(value = "/transfer")
    public ResponseEntity<Transaction> transfer(
        @RequestBody AccountActionDto accountActionDto) {
            return new ResponseEntity<>(
                accountService.transfer(accountActionDto.getUsername(), 
                    accountActionDto.getPassword(), accountActionDto.getAmount(), accountActionDto.getRecipientAccoutNo()), 
                HttpStatus.OK);
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<BigDecimal> getBalance(
        @RequestParam String username,
        @RequestParam String password) {
            return new ResponseEntity<>(accountService.getBalance(username, password), HttpStatus.OK);
    }

    @GetMapping(value = "/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(

        @RequestParam String username,
        @RequestParam String password) {
            return new ResponseEntity<>(accountService.getTransactions(username, password), HttpStatus.OK);
    }

    
}
