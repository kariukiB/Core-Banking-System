package com.kariuki.banking.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Loan {
    private Long id;
    private String loanAccount;
    private String relatedAccount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal outStandingAmount;
    private BigDecimal installmentAmount;
    private Integer numberOfInstallments;
    private BigDecimal availableFunds;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
}
