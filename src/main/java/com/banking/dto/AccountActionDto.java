package com.banking.dto;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountActionDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private BigDecimal amount;
}
