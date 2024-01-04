package com.starling.challenge.inboundapicontrollers.v1;

import com.starling.challenge.domain.model.challenge.RoundupRequest;
import com.starling.challenge.domain.model.challenge.RoundupResponse;
import com.starling.challenge.domain.services.challenge.RoundupServiceInt;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/v1/retroactive-roundup")
public class RetroactiveRoundupController {

    private final RoundupServiceInt roundupService;

    public RetroactiveRoundupController(
        RoundupServiceInt roundupService
    ){
        this.roundupService = roundupService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RoundupResponse> roundUpForWeek(RoundupRequest roundupRequest){
        return roundupService.roundupForWeek(roundupRequest);
    }
    
}
