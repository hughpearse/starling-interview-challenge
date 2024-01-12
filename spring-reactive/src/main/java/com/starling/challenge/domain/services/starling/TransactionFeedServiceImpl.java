package com.starling.challenge.domain.services.starling;

import org.springframework.stereotype.Service;

import com.starling.challenge.domain.model.starling.FeedItems;
import com.starling.challenge.outboundclients.starling.TransactionFeedClient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;
import java.util.Calendar;

/**
 * Transaction Feed Domain Service
 */
@Service
@Slf4j
@AllArgsConstructor
public class TransactionFeedServiceImpl implements TransactionFeedService {

    private TransactionFeedClient transactionFeedClient;

    public Mono<FeedItems> getTransactionFeed(
        UUID accountUid,
        Date weekStarting,
        Date weekEnding
    ){
        log.info("Getting transaction feed");
        return transactionFeedClient.getTransactionFeed(
            accountUid, 
            weekStarting, 
            weekEnding)
            .doOnSuccess(feedItems -> log.info("Transaction feed found."))
            .doOnError(t -> log.info("Transaction feed not found."));
    }    

    public Mono<FeedItems> getTransactionFeedForWeek(
        UUID accountUid,
        Date weekStarting
        ) {
        log.info("Getting transaction feed for week");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(weekStarting);
        calendar.add(Calendar.DATE, 7);
        Date weekEnding = calendar.getTime();
        return getTransactionFeed(
            accountUid, 
            weekStarting, 
            weekEnding);
    }
    
}
