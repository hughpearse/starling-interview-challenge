package com.starling.challenge.outboundclients.starling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigInteger;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;
import com.starling.challenge.exceptions.StarlingRuntimeException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Service for interactig with Starling accounts.
 */
@Service
@Slf4j
public class AccountsClient {

    private BaseHttpClient baseHttpClient;
    private String listaccnumsurl;
    private String confirmationoffundsurl;
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor for Starling accounts HTTP client.
     * @param authService injected HTTP client
     * @param listaccnumsurl injected URL for getting all accounts
     * @param confirmationoffundsurl injected URL for checking if funds are present
     */
    public AccountsClient(
        BaseHttpClient baseHttpClient,
        @Value("${outboundclients.starling.accounts.listaccnums.resource}")
        String listaccnumsurl,
        @Value("${outboundclients.starling.accounts.confirmationoffunds.resource}")
        String confirmationoffundsurl
    ){
        this.baseHttpClient = baseHttpClient;
        this.listaccnumsurl = listaccnumsurl;
        this.confirmationoffundsurl = confirmationoffundsurl;
    }

    /**
     * Get all accounts.
     * @return Accounts object
     */
    public Mono<Accounts> getAccounts() {
        try {
            log.info("Getting all accounts.");
            return baseHttpClient
            .getClient()
            .get()
            .uri(listaccnumsurl)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Accounts.class)
            .doOnError(WebClientResponseException.class, throwable -> {
                if (throwable.getStatusCode().is4xxClientError()) {
                    throw new StarlingRuntimeException(throwable);
                }
            });
        } catch (StarlingRuntimeException ex) {
            log.error("Failed to get accounts.");
            throw ex;
        }
    }

    /**
     * Check user has funds to transfer.
     * @param accountUid account UUID
     * @param targetAmountInMinorUnits amount to transfer
     * @return ConfirmationOfFundsResponse object
     */
    public Mono<ConfirmationOfFundsResponse> getConfirmationOfFunds(
        UUID accountUid,
        BigInteger targetAmountInMinorUnits
        ) {
        try {
            log.info("Confirming funds are present in account {}", accountUid);
            return baseHttpClient
            .getClient()
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(confirmationoffundsurl)
                .queryParam("targetAmountInMinorUnits", targetAmountInMinorUnits)
                .build(accountUid))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(ConfirmationOfFundsResponse.class)
            .doOnError(WebClientResponseException.class, throwable -> {
                if (throwable.getStatusCode().is4xxClientError()) {
                    throw new StarlingRuntimeException(throwable);
                }
            });
        } catch (StarlingRuntimeException ex) {
            log.error("Failed to confirm funds present.");
            throw ex;
        }
    }
}
