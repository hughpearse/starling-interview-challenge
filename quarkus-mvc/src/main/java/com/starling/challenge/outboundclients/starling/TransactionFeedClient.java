package com.starling.challenge.outboundclients.starling;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

import com.starling.challenge.domain.model.starling.FeedItems;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@RegisterClientHeaders(RequestStarlingHeaderFactory.class)
public interface TransactionFeedClient {

    @GET
    @Path("/feed/account/{accountUid}/settled-transactions-between")
    public FeedItems getTransactionFeed(@PathParam("accountUid") UUID accountUid, @QueryParam("minTransactionTimestamp") String minTransactionTimestamp, @QueryParam("maxTransactionTimestamp") String maxTransactionTimestamp);
}
