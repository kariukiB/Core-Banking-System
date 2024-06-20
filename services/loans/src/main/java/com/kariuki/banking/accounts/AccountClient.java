package com.kariuki.banking.accounts;

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
    @Value("${application.config.accounts-url}")
    private String accountUrl;
    private final RestTemplate restTemplate;
    public ResponseEntity<AccountsResponse> getAccount(String accountNumber) throws AccountNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<AccountsResponse> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<AccountsResponse> responseEntity = restTemplate.exchange(
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
    public void saveUpdatedAccount(AccountsResponse accountResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpEntity<AccountsResponse> entity = new HttpEntity<>(accountResponse, headers);
        restTemplate.exchange(
                accountUrl + "/resave",
                HttpMethod.POST,
                entity,
                AccountsResponse.class
        );
    }
}
