package com.starling.challenge.outboundclients.starling;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

import com.starling.challenge.domain.model.starling.CreateOrUpdateSavingsGoalResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalRequestV2;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalsV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@RegisterClientHeaders(RequestStarlingHeaderFactory.class)
public interface SavingsGoalsClient {

    @GET
    @Path("/account/{accountUid}/savings-goals")
    public Uni<SavingsGoalsV2> getSavingsGoals(@PathParam("accountUid") UUID accountUid);

    @PUT
    @Path("/account/{accountUid}/savings-goals")
    public Uni<CreateOrUpdateSavingsGoalResponseV2> createSavingsGoal(@PathParam("accountUid") UUID accountUid, SavingsGoalRequestV2 savingsGoalRequestV2);

    @PUT
    @Path("/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}")
    public Uni<SavingsGoalTransferResponseV2> transferToSavingsGoal(@PathParam("accountUid") UUID accountUid, @PathParam("savingsGoalUid") UUID savingsGoalUid, @PathParam("transferUid") UUID transferUid, TopUpRequestV2 topUpRequestV2);

}
