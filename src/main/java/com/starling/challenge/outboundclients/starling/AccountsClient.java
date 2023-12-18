package com.starling.challenge.outboundclients.starling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.UUID;

import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;
import com.starling.challenge.exceptions.StarlingRuntimeException;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for interactig with Starling accounts.
 */
@Service
@Slf4j
public class AccountsClient {

    private BaseHttpClient baseHttpClient;
    private String listaccnumsurl;
    private String confirmationoffundsurl;

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
    public Accounts getAccounts() {
        log.info("Getting all accounts.");
        return baseHttpClient
        .getClient()
        .get()
        .uri(listaccnumsurl)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> { 
            throw new StarlingRuntimeException(response.getStatusCode(), response.getHeaders(), response.getBody());
        })
        .body(Accounts.class);
    }

    /**
     * Check user has funds to transfer.
     * @param accountUid account UUID
     * @return ConfirmationOfFundsResponse object
     */
    public ConfirmationOfFundsResponse getConfirmationOfFunds(UUID accountUid) {
        log.info("Confirming funds are present in account {}", accountUid);
        return baseHttpClient
        .getClient()
        .get()
        .uri(confirmationoffundsurl, accountUid)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> { 
            throw new StarlingRuntimeException(response.getStatusCode(), response.getHeaders(), response.getBody());
        })
        .body(ConfirmationOfFundsResponse.class);
    }
    
}
