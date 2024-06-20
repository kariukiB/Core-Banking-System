package com.kariuki.banking.controller;

import com.kariuki.banking.model.Loan;
import com.kariuki.banking.model.LoanRequest;
import com.kariuki.banking.service.LoansService;
import com.kariuki.banking.utils.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoansController {
    private final LoansService service;

    @PostMapping
    public Response<Loan> disburseLoan(@RequestBody LoanRequest request) throws AccountNotFoundException {
        return service.issueLoan(request);
    }
    @PostMapping("/resave")
    public void updateLoan(@RequestBody Loan request) {
        service.updateAccount(request);
    }
}
