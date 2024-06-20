package com.kariuki.banking.service;

import com.kariuki.banking.account.AccountClient;
import com.kariuki.banking.models.CreditDebitRequest;
import com.kariuki.banking.models.LoanDisburseRequest;
import com.kariuki.banking.models.Transaction;
import com.kariuki.banking.models.TransferRequest;
import com.kariuki.banking.repositories.TransactionRepository;
import com.kariuki.banking.utils.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

import static com.kariuki.banking.models.TransactionType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;
    private final AccountClient client;

    public Response<BigDecimal> addMoney(CreditDebitRequest request) throws AccountNotFoundException {
        //Validate account
        var account = client.getAccount(request.getAccountNumber()).getBody();
        //Increase balance
        assert account != null;
        account.setBalance(account.getBalance().add(request.getAmount()));
        //Save account with new balance
        client.saveUpdatedAccount(account);
        //Save transaction
        repository.save(Transaction.builder()
                        .accountNumber(request.getAccountNumber())
                        .amount(request.getAmount())
                        .transactionType(CREDIT)
                .build());
        return Response.<BigDecimal>builder()
                .entity(account.getBalance())
                .statusCode(HttpStatus.ACCEPTED.value())
                .message(String.format("Transaction of %2f added successfully to %s . New balance is: %2f", request.getAmount(),request.getAccountNumber(),(account.getBalance())))
                .build();
    }

    public Response<BigDecimal> removeMoney(CreditDebitRequest request) throws AccountNotFoundException {
        //Validate account
        var debitAccount = client.getAccount(request.getAccountNumber()).getBody();
        //Validate balance !> request amount
        assert debitAccount != null;
        if (debitAccount.getBalance().compareTo(request.getAmount()) < 0){
            return Response.<BigDecimal>builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Insufficient funds for this request")
                    .build();
        }
        //Decrease balance
        debitAccount.setBalance(debitAccount.getBalance().subtract(request.getAmount()));
        //Save account with new balance
        client.saveUpdatedAccount(debitAccount);
        //Save transaction
        repository.save(Transaction.builder()
                .accountNumber(request.getAccountNumber())
                .amount(request.getAmount())
                .transactionType(DEBIT)
                .build());
        return Response.<BigDecimal>builder()
                .entity(debitAccount.getBalance())
                .statusCode(HttpStatus.ACCEPTED.value())
                .message(String.format("Transaction of %2f withdrawn successfully from %s . New balance is: %2f", request.getAmount(),request.getAccountNumber(),(debitAccount.getBalance())))
                .build();
    }
    public ResponseEntity<String> moveMoney(TransferRequest request) throws AccountNotFoundException {
        //Validate credit account
        var creditAccount = client.getAccount(request.getToAccount()).getBody();
        //Validate debit account
        var debitAccount = client.getAccount(request.getFromAccount()).getBody();
        //Validate balance !> request amount
        assert creditAccount != null;
        if (debitAccount == null) throw new AssertionError("Invalid source account");
        if (debitAccount.getBalance().compareTo(request.getAmount()) < 0){
            return new ResponseEntity<>("Insufficient funds for this request", HttpStatus.BAD_REQUEST);

        }
        //Debit source account
        debitAccount.setBalance(debitAccount.getBalance().subtract(request.getAmount()));
        //Save debit account with new balance
        client.saveUpdatedAccount(debitAccount);
        //Save transaction
        repository.save(Transaction.builder()
                .accountNumber(request.getFromAccount())
                .amount(request.getAmount())
                .transactionType(TRANSFER)
                .build());
        //Credit beneficiary account
        creditAccount.setBalance(creditAccount.getBalance().add(request.getAmount()));
        //Save credit account with new balance
        client.saveUpdatedAccount(creditAccount);
        //Save transaction
        repository.save(Transaction.builder()
                .accountNumber(request.getToAccount())
                .amount(request.getAmount())
                .transactionType(TRANSFER)
                .build());
        return new ResponseEntity<>(
                String.format("Transfer transaction of %.2f posted from %s to %s",
                        request.getAmount(),
                        debitAccount.getCustomerName(),
                        creditAccount.getCustomerName()),
                HttpStatus.ACCEPTED
        );

    }
    public ResponseEntity<String> disburseLoan(LoanDisburseRequest request) throws AccountNotFoundException {
        //Validate credit account
        var creditAccount = client.getAccount(request.getToAccount()).getBody();
        //Validate debit account
        var debitAccount = request.getFromAccount() ;

        //Debit source account

        debitAccount.setAvailableFunds(BigDecimal.ZERO);
        //Save debit account with new balance
        client.saveUpdatedLoan(debitAccount);
        //Save transaction
        repository.save(Transaction.builder()
                .accountNumber(debitAccount.getLoanAccount())
                .amount(request.getAmount())
                .transactionType(LOAN_DISBURSEMENT)
                .build());
        //Credit beneficiary account
        assert creditAccount != null;
        creditAccount.setBalance(creditAccount.getBalance().add(request.getAmount()));
        //Save credit account with new balance
        client.saveUpdatedAccount(creditAccount);
        //Save transaction
        repository.save(Transaction.builder()
                .accountNumber(request.getToAccount())
                .amount(request.getAmount())
                .transactionType(LOAN_DISBURSEMENT)
                .build());
        return new ResponseEntity<>(
                String.format("Transfer transaction of %.2f posted from %s to %s",
                        request.getAmount(),
                        debitAccount.getLoanAccount(),
                        creditAccount.getAccountNumber()),
                HttpStatus.ACCEPTED
        );

    }


}
