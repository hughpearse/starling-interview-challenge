package com.starling.challenge.outboundclients.starling;

import java.math.BigInteger;
import java.util.UUID;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@RegisterClientHeaders(RequestStarlingHeaderFactory.class)
public interface AccountsClient {
    
    @GET
    @Path("/accounts")
    public Accounts getAccounts();

    @GET
    @Path("/accounts/{accountUid}/confirmation-of-funds")
    public ConfirmationOfFundsResponse getConfirmationOfFunds(@PathParam("accountUid") UUID accountUid, @QueryParam("targetAmountInMinorUnits") BigInteger targetAmountInMinorUnits);

}
