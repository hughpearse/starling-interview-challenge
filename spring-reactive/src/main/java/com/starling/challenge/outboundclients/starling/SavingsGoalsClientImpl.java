package com.starling.challenge.outboundclients.starling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.UUID;

import com.starling.challenge.domain.model.starling.CreateOrUpdateSavingsGoalResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalRequestV2;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalsV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;
import com.starling.challenge.exceptions.StarlingRuntimeException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Service for interactig with Starling savings goals.
 */
@Service
@Slf4j
public class SavingsGoalsClientImpl implements SavingsGoalsClient {
    private BaseHttpClient baseHttpClient;
    private String savingsgeturl;
    private String savingscreateurl;
    private String savingstransferurl;

    /**
     * Constructor for Starling savings goals HTTP client.
     * @param baseHttpClient injected HTTP client
     * @param savingsgeturl injected URL for getting savings goals
     * @param savingscreateurl injected URL for creating savings goals
     * @param savingstransferurl injected URL for transferring money to savings goals
     */
    public SavingsGoalsClientImpl(
        BaseHttpClient baseHttpClient,
        @Value("${outboundclients.starling.savingsgoals.get.resource}")
        String savingsgeturl,
        @Value("${outboundclients.starling.savingsgoals.create.resource}")
        String savingscreateurl,
        @Value("${outboundclients.starling.savingsgoals.transfer.resource}")
        String savingstransferurl
    ){
        this.baseHttpClient = baseHttpClient;
        this.savingsgeturl = savingsgeturl;
        this.savingscreateurl = savingscreateurl;
        this.savingstransferurl = savingstransferurl;
    }

    public Mono<SavingsGoalsV2> getSavingsGoals(UUID accountUid) {
        try {
            log.info("Getting savings goals");
            return baseHttpClient
            .getClient()
            .get()
            .uri(savingsgeturl, accountUid)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(SavingsGoalsV2.class)
            .doOnError(WebClientResponseException.class, throwable -> {
                if (throwable.getStatusCode().is4xxClientError()) {
                    throw new StarlingRuntimeException(throwable);
                }
            });
        } catch (StarlingRuntimeException ex) {
            log.error("Failed to get savings goals.");
            throw ex;
        }
    }

    public Mono<CreateOrUpdateSavingsGoalResponseV2> createSavingsGoal(
        UUID accountUid,
        SavingsGoalRequestV2 savingsGoalRequestV2
    ) {
        try {
            log.info("Creating savings goal: {}", savingsGoalRequestV2.getName());
            return baseHttpClient
            .getClient()
            .put()
            .uri(savingscreateurl, accountUid)
            .body(BodyInserters.fromValue(savingsGoalRequestV2))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(CreateOrUpdateSavingsGoalResponseV2.class)
            .doOnError(WebClientResponseException.class, throwable -> {
                if (throwable.getStatusCode().is4xxClientError()) {
                    throw new StarlingRuntimeException(throwable);
                }
            });
        } catch (StarlingRuntimeException ex) {
            log.error("Failed to create savings goal.");
            throw ex;
        }
    }
    
    public Mono<SavingsGoalTransferResponseV2> transferToSavingsGoal(
        UUID accountUid,
        UUID savingsGoalUid,
        UUID transferUid,
        TopUpRequestV2 topUpRequestV2
    ) {
        try {
            log.info("Transferring {} from account {} to savings goal {}.", topUpRequestV2.getAmount(), accountUid, savingsGoalUid);
            return baseHttpClient
            .getClient()
            .put()
            .uri(savingstransferurl, accountUid, savingsGoalUid, transferUid)
            .bodyValue(topUpRequestV2)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(SavingsGoalTransferResponseV2.class)
            .doOnError(WebClientResponseException.class, throwable -> {
                if (throwable.getStatusCode().is4xxClientError()) {
                    throw new StarlingRuntimeException(throwable);
                }
            });
        } catch (StarlingRuntimeException ex) {
            log.error("Failed to transfer to savings goal.");
            throw ex;
        }
    }
}
