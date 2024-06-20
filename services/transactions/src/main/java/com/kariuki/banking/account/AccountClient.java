package com.kariuki.banking.account;

import com.kariuki.banking.models.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.AccountNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountClient {
    private final RestTemplate restTemplate;

    @Value(value = "${application.config.accounts-url}")
    private String accountUrl;
    @Value("${application.config.loans-url}")
    private String loanUrl;
    public ResponseEntity<AccountResponse> getAccount(String accountNumber) throws AccountNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<AccountResponse> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<AccountResponse> responseEntity = restTemplate.exchange(
                accountUrl + "/validate?accountNumber=" + accountNumber,
                HttpMethod.GET,
                entity,
                responseType
        );
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity;
        } else {
            throw new AccountNotFoundException(String.format("Account with number %s not found", accountNumber));
        }
    }
    public void saveUpdatedAccount(AccountResponse accountResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpEntity<AccountResponse> entity = new HttpEntity<>(accountResponse, headers);
        restTemplate.exchange(
                accountUrl + "/resave",
                HttpMethod.POST,
                entity,
                AccountResponse.class
        );
    }
    public void saveUpdatedLoan(Loan accountResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpEntity<Loan> entity = new HttpEntity<>(accountResponse, headers);
        restTemplate.exchange(
                loanUrl + "/resave",
                HttpMethod.POST,
                entity,
                AccountResponse.class
        );
    }
}
