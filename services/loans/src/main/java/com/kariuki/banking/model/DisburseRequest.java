package com.kariuki.banking.model;

import com.kariuki.banking.transactions.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class DisburseRequest {
    private Loan fromAccount;
    private String toAccount;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}
