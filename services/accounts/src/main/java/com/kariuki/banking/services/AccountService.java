package com.kariuki.banking.services;


import com.kariuki.banking.models.Account;
import com.kariuki.banking.models.AccountRequest;
import com.kariuki.banking.repositories.AccountRepo;
import com.kariuki.banking.utils.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepo repository;

    public Response<Account> createAccount(AccountRequest request) {
        if (repository.existsByCustomerId(request.getCustomerId())){
            return Response.<Account>builder()
                    .message(String.format("Customer with id %s already exists", request.getCustomerId()))
                    .entity(null)
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .build();
        }
        Account account = Account.builder()
                .customerName(request.getCustomerName())
                .customerId(request.getCustomerId())
                .customerEmail(request.getCustomerEmail())
                .customerPhone(request.getCustomerPhone())
                .accountNumber(generateAccountNumber())
                .build();
        repository.save(account);

        return Response.<Account>builder()
                .entity(account)
                .message("Account creation success")
                .statusCode(HttpStatus.OK.value())
                .build();
    }
    public static String generateAccountNumber() {
        String year = String.valueOf(Year.now());
        int min = 1000;
        int max = 9999;
        String sol = "0040";
         int number = (int)Math.floor(Math.random() * (max- min + 1)) + min;
         return sol + year + number;
    }

    public Response<List<Account>> retrieveAllAccounts() {
        var accounts = repository.findAll();
        return Response.<List<Account>>builder()
                .statusCode(HttpStatus.OK.value())
                .entity(accounts)
                .message("Accounts Retrieved successfully")
                .build();
    }

    public Response<Account> retrieveAccount(Long customerId) {
        var account = repository.findById(customerId);
        if (account.isPresent()) {
            return Response.<Account>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Account Retrieved successfully")
                    .entity(account.get())
                    .build();
        } else {
            return Response.<Account>builder()
                    .entity(null)
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message("Account not found")
                    .build();
        }
    }

    public Response<BigDecimal> getBalance(String accountNumber) {
            var account = repository.findByAccountNumber(accountNumber);
            if (account.isEmpty()){
                return Response.<BigDecimal>builder()
                        .message("Provided account number does not exist")
                        .statusCode(HttpStatus.I_AM_A_TEAPOT.value())
                        .build();
            } else {
                return Response.<BigDecimal>builder()
                        .message(String.format("Account balance for %s, %s retrieved success", accountNumber, account.get().getCustomerName()))
                        .statusCode(HttpStatus.FOUND.value())
                        .entity(account.get().getBalance())
                        .build();
            }

    }

    public ResponseEntity<Account >fetchAccount(String accountNumber) {
       return ResponseEntity.ok( repository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found")));
    }
    public void updateAccount(Account account) {
        repository.save(account);
    }
}
