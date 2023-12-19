package com.starling.challenge.domain.services.starling;

import java.util.Date;

import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.FeedItems;

/**
 * Transaction Feed Domain Service
 */
public interface TransactionFeedServiceInt {

    /**
     * Get transaction feed
     * @param account the AccountV2 object
     * @param minTransactionTimestamp lower bound on time frame
     * @param maxTransactionTimestamp upper bound on time frame
     * @return the FeedItems object
     */
    public FeedItems getTransactionFeed(AccountV2 account, Date weekStarting, Date weekEnding);
    
    /**
     * Get transaction feed for a week
     * @param account the AccountV2 object
     * @param minTransactionTimestamp lower bound on time frame (starting day)
     * @return the FeedItems object
     */
    public FeedItems getTransactionFeedForWeek(AccountV2 account, Date weekStarting);
}
