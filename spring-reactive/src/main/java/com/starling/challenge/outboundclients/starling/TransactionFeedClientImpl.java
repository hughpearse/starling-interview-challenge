package com.starling.challenge.outboundclients.starling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import java.util.UUID;

import com.starling.challenge.domain.model.starling.FeedItems;
import com.starling.challenge.exceptions.StarlingRuntimeException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Service for interactig with Starling transaction feed.
 */
@Service
@Slf4j
public class TransactionFeedClientImpl implements TransactionFeedClient {

    private BaseHttpClient baseHttpClient;
    private String gettransactionsurl;

    /**
     * Constructor for Starling transaction feed HTTP client.
     * @param baseHttpClient injected HTTP client
     * @param gettransactionsurl injected URL for getting transactions
     */
    public TransactionFeedClientImpl(
        BaseHttpClient baseHttpClient,
        @Value("${outboundclients.starling.transactionfeed.getsettledinrange.resource}")
        String gettransactionsurl
    ){
        this.baseHttpClient = baseHttpClient;
        this.gettransactionsurl = gettransactionsurl;
    }
    
    public Mono<FeedItems> getTransactionFeed(
        UUID accountUid,
        Date minTransactionTimestamp,
        Date maxTransactionTimestamp
        ) {
        try {
            log.info("Getting transactions from account {} in range from {} to {}", accountUid, minTransactionTimestamp.getTime(), maxTransactionTimestamp.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String isoDate_minTransactionTimestamp = sdf.format(minTransactionTimestamp);
            String isoDate_maxTransactionTimestamp = sdf.format(maxTransactionTimestamp);
            return baseHttpClient
            .getClient()
            .get()
            .uri(uriBuilder -> uriBuilder
            .path(gettransactionsurl)
            .queryParam("minTransactionTimestamp", isoDate_minTransactionTimestamp)
            .queryParam("maxTransactionTimestamp", isoDate_maxTransactionTimestamp)
            .build(accountUid))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(FeedItems.class)
            .doOnError(WebClientResponseException.class, throwable -> {
                if (throwable.getStatusCode().is4xxClientError()) {
                    throw new StarlingRuntimeException(throwable);
                }
            });
        } catch (StarlingRuntimeException ex) {
            log.error("Failed to get transaction feed.");
            throw ex;
        }
    }
}
