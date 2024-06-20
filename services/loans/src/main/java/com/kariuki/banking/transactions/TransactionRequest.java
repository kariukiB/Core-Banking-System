package com.kariuki.banking.transactions;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionRequest {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}
