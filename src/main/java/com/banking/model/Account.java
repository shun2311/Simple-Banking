package com.banking.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNo;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(max = 128)
    private String password;
    
    @DecimalMin(value = "0")
    @Column(precision = 19, scale = 6, name = "amount", nullable = false)
    private BigDecimal amount;

}
