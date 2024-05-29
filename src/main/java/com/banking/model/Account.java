package com.banking.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "account")
public class Account {
    @Id
    private Long accountNo;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    
    @NotNull
    private BigDecimal amount;

}
