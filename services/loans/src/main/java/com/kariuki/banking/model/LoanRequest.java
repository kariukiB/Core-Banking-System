package com.kariuki.banking.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequest {
    private String relatedAccount;
    private BigDecimal principal;
    private Integer numberOfInstallments;
}
