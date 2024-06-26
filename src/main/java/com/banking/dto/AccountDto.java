package com.banking.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountDto {
    @NotNull
    private Long accountNo;

    @NotBlank
    private String username;

    @NotNull
    private BigDecimal amount;
}
