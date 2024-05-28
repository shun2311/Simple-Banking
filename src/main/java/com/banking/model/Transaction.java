package com.banking.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

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
    @Column(name = "initiator")
    private Long initiator;

    @Column(name = "recipient")
    private Long recipient;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountNo")
    @JoinColumn(name = "initiator", referencedColumnName = "accountNo")
    private Account initiatorAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountNo")
    @JoinColumn(name = "recipient", referencedColumnName = "accountNo")
    private Account recipientAccount;
}
