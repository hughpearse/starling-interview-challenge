package com.starling.challenge.domain.services.starling;

import org.springframework.stereotype.Service;

import com.starling.challenge.domain.model.starling.FeedItems;
import com.starling.challenge.outboundclients.starling.TransactionFeedClient;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;
import java.util.Calendar;

/**
 * Transaction Feed Domain Service
 */
@Service
@Slf4j
public class TransactionFeedService implements TransactionFeedServiceInt {

    private TransactionFeedClient transactionFeedClient;

    /**
     * Constructor for Transaction Feed domain service.
     * @param transactionFeedClient injected HTTP client for interacting with transaction feeds.
     */
    public TransactionFeedService(
        TransactionFeedClient transactionFeedClient
    ){
        this.transactionFeedClient = transactionFeedClient;
    }

    public FeedItems getTransactionFeed(
        UUID accountUid,
        Date weekStarting,
        Date weekEnding
    ){
        FeedItems transactionFeed = transactionFeedClient.getTransactionFeed(
            accountUid, 
            weekStarting, 
            weekEnding);
        log.info("Got transaction feed.");
        return transactionFeed;
    }

    public FeedItems getTransactionFeedForWeek(
        UUID accountUid,
        Date weekStarting
        ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(weekStarting);
        calendar.add(Calendar.DATE, 7);
        Date weekEnding = calendar.getTime();
        FeedItems transactionFeed = getTransactionFeed(
            accountUid, 
            weekStarting, 
            weekEnding);
        return transactionFeed;
    }
    
}
