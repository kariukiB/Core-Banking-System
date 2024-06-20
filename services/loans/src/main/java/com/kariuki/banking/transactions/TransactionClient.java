package com.kariuki.banking.transactions;

import com.kariuki.banking.model.DisburseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.springframework.http.HttpMethod.POST;

@Service
@RequiredArgsConstructor
public class TransactionClient {
    private final RestTemplate restTemplate;
    @Value("${application.config.transactions-url}")
    private String transactionsUrl;
    public ResponseEntity<BigDecimal> transfer(DisburseRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpEntity<DisburseRequest> requestEntity = new HttpEntity<>(request, headers);
        ParameterizedTypeReference<BigDecimal> responseType = new ParameterizedTypeReference<>() {};
       ResponseEntity<BigDecimal> response = restTemplate.exchange(
                transactionsUrl + "/disburse-loan",
                POST,
                requestEntity,
                responseType
        );
        if (response.getStatusCode().isError()){
            throw new RuntimeException("Error occurred while disbursing loan");
        }
        return response;
    }


}
