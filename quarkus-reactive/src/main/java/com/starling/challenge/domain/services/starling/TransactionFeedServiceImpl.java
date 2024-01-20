package com.starling.challenge.domain.services.starling;

import com.starling.challenge.config.MicroProfileConfig;
import com.starling.challenge.domain.model.starling.FeedItems;
import com.starling.challenge.outboundclients.starling.BaseHttpClientImpl;
import com.starling.challenge.outboundclients.starling.TransactionFeedClient;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Transaction Feed Domain Service
 */
@ApplicationScoped
@Slf4j
public class TransactionFeedServiceImpl implements TransactionFeedService {

    private TransactionFeedClient transactionFeedClient;

    public TransactionFeedServiceImpl(MicroProfileConfig microProfileConfig) {
        this.transactionFeedClient = new BaseHttpClientImpl<>(microProfileConfig.domain(), TransactionFeedClient.class).getService();
    }

    public Uni<FeedItems> getTransactionFeed(
        UUID accountUid,
        Date weekStarting,
        Date weekEnding
    ){
        log.info("Getting transaction feed");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String isoDate_minTransactionTimestamp = sdf.format(weekStarting);
        String isoDate_maxTransactionTimestamp = sdf.format(weekEnding);
        return transactionFeedClient.getTransactionFeed(
                accountUid, 
                isoDate_minTransactionTimestamp, 
                isoDate_maxTransactionTimestamp)
                .onItem().ifNotNull().invoke(feedItems -> log.info("Got transaction feed."));
    }

    public Uni<FeedItems> getTransactionFeedForWeek(
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
