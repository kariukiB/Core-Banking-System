package com.kariuki.banking.controllers;

import com.kariuki.banking.models.CreditDebitRequest;
import com.kariuki.banking.models.LoanDisburseRequest;
import com.kariuki.banking.models.TransferRequest;
import com.kariuki.banking.service.TransactionService;
import com.kariuki.banking.utils.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v2/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/credit")
    public Response<BigDecimal> creditAccount(@RequestBody CreditDebitRequest request) throws AccountNotFoundException {
        return service.addMoney(request);
    }

    @PostMapping("/debit")
    public Response<BigDecimal> debitAccount(@RequestBody CreditDebitRequest request) throws AccountNotFoundException {
        return service.removeMoney(request);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferService(@RequestBody TransferRequest request) throws AccountNotFoundException {
        return service.moveMoney(request);
    }
    @PostMapping("/disburse-loan")
    public ResponseEntity<String> disburseLoan(@RequestBody LoanDisburseRequest request) throws AccountNotFoundException {
        return service.disburseLoan(request);
    }
}
