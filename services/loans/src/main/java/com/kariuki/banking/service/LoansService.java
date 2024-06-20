package com.kariuki.banking.service;

import com.kariuki.banking.accounts.AccountClient;
import com.kariuki.banking.accounts.AccountsResponse;
import com.kariuki.banking.model.DisburseRequest;
import com.kariuki.banking.model.Loan;
import com.kariuki.banking.model.LoanRequest;
import com.kariuki.banking.repository.LoansRepository;
import com.kariuki.banking.transactions.TransactionClient;
import com.kariuki.banking.transactions.TransactionType;
import com.kariuki.banking.utils.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Year;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoansService {
    private final LoansRepository repository;
    private final AccountClient client;
    private final TransactionClient transactionClient;
    @Value("${application.config.interest-rate}")
    private  Double interestRate;
    public Response<Loan> issueLoan(LoanRequest request) throws AccountNotFoundException {
        Response<Loan> response = new Response<>();
        //Validate Related Account
        AccountsResponse account = this.client.getAccount(request.getRelatedAccount()).getBody();

        //Create a new loan account
        var interest = BigDecimal.valueOf(interestRate / request.getNumberOfInstallments()).multiply(request.getPrincipal());
        var principal = request.getPrincipal().add(interest);
        Loan newLoan = Loan.builder()
                .relatedAccount(request.getRelatedAccount())
                .interest(interest)
                .principal(request.getPrincipal())
                .installmentAmount(principal.divide(BigDecimal.valueOf(request.getNumberOfInstallments()),new MathContext(3)))
                .numberOfInstallments(request.getNumberOfInstallments())
                .loanAccount(generateAccountNumber())
                .outStandingAmount(principal)
                .availableFunds(request.getPrincipal())
                .build();
       repository.save(newLoan);
        //Credit account with loan funds
        assert account != null;
        log.info("__________CREDITING ACCOUNT_____ {}", account.getAccountNumber());
        try {

            this.transactionClient.transfer(DisburseRequest.builder()
                    .amount(newLoan.getAvailableFunds())
                    .fromAccount(newLoan)
                    .toAccount(account.getAccountNumber())
                    .transactionType(TransactionType.LOAN_DISBURSEMENT)
                    .build());
            this.client.saveUpdatedAccount(account);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

       response.setEntity(newLoan);
       response.setMessage("Successfully issued the loan to %s".formatted(request.getRelatedAccount()) + "Loan account is %s".formatted(newLoan.getLoanAccount()));
       response.setStatusCode(HttpStatus.OK.value());
       return response;
    }

    private static String generateAccountNumber() {
        String year = String.valueOf(Year.now());
        int min = 100;
        int max = 999;
        String sol = "004LA";
        int number = (int)Math.floor(Math.random() * (max- min + 1)) + min;
        return sol + year + number;
    }
    public void updateAccount(Loan account) {
        repository.save(account);
    }
}
