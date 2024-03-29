package com.starling.challenge.domain.services.starling;

import java.util.Date;
import java.util.UUID;

import com.starling.challenge.domain.model.starling.FeedItems;

import reactor.core.publisher.Mono;

/**
 * Transaction Feed Domain Service
 */
public interface TransactionFeedService {

    /**
     * Get transaction feed
     * @param accountUid the account UUID
     * @param minTransactionTimestamp lower bound on time frame
     * @param maxTransactionTimestamp upper bound on time frame
     * @return the FeedItems object
     */
    public Mono<FeedItems> getTransactionFeed(UUID accountUid, Date weekStarting, Date weekEnding);
    
    /**
     * Get transaction feed for a week
     * @param accountUid the account UUID
     * @param minTransactionTimestamp lower bound on time frame (starting day)
     * @return the FeedItems object
     */
    public Mono<FeedItems> getTransactionFeedForWeek(UUID accountUid, Date weekStarting);
}
