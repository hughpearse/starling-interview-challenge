package com.starling.challenge.outboundclients.starling;

import java.util.Date;
import java.util.UUID;

import com.starling.challenge.domain.model.starling.FeedItems;

import reactor.core.publisher.Mono;

public interface TransactionFeedClient {
    /**
     * Get transaction feed for a given time frame
     * @param accountUid account UUID
     * @param minTransactionTimestamp lower bound on time frame
     * @param maxTransactionTimestamp upper bound on time frame
     * @return
     */
    public Mono<FeedItems> getTransactionFeed(UUID accountUid, Date minTransactionTimestamp, Date maxTransactionTimestamp);
}
