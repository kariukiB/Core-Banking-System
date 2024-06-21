package com.kariuki.banking.controllers;


import com.kariuki.banking.models.Account;
import com.kariuki.banking.models.AccountRequest;
import com.kariuki.banking.services.AccountService;
import com.kariuki.banking.utils.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v2/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;

    @PostMapping
    public Response<Account> createAccount(@RequestBody AccountRequest request) {
        return service.createAccount(request);
    }

    @GetMapping
    public Response<List<Account>> getAllAccounts() {
        return service.retrieveAllAccounts();
    }

    @GetMapping("/{customerId}")
    public Response<Account> getAccount(@PathVariable("customerId") Long customerId) {
        return service.retrieveAccount(customerId);
    }
    @GetMapping("/{accountNumber}")
    public Response<BigDecimal> getAccountBalance(@PathVariable("accountNumber") String accountNumber) {
        return service.getBalance(accountNumber);
    }
    @GetMapping("/validate")
    public ResponseEntity<Account> validateAccount(@RequestParam String accountNumber) {
        return service.fetchAccount(accountNumber);
    }
    @PostMapping("/resave")
    public void resaveAccount(@RequestBody Account request) {
        service.updateAccount(request);
    }
}
