package com.kariuki.banking.account;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
