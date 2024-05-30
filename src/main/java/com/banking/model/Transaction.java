package com.banking.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "transcation")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Long initiator;

    private Long recipient;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountNo")
    @JoinColumn(name = "initiator", referencedColumnName = "accountNo")
    @JsonIgnore
    private Account initiatorAccount;
}
