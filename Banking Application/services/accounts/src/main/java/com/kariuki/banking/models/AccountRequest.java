package com.kariuki.banking.models;

import lombok.Data;

@Data
public class AccountRequest {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerId;
}
