package com.starling.challenge.inboundapicontrollers.v1;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starling.challenge.domain.model.challenge.RoundupRequest;
import com.starling.challenge.domain.model.challenge.RoundupResponse;
import com.starling.challenge.domain.services.challenge.RoundupServiceInt;

/**
 * Router for directing HTTP requests to the appropriate logic.
 */
@RestController
public class RetroactiveRoundupController {
    private final RoundupServiceInt roundupService;

    public RetroactiveRoundupController(
        RoundupServiceInt roundupService
    ){
        this.roundupService = roundupService;
    }

    /**
     * Allows user to select a week in the past and retroactively round up their transactions into a savings goal.
     * @param roundupRequest the request as a RoundupRequest object
     * @return the amount rounded up as SavingsGoalTransferResponseV2 object
     */
    @RequestMapping("/v1/retroactive-roundup")
    public RoundupResponse roundUpForWeek(
        @RequestBody
        RoundupRequest roundupRequest
    ){
        return roundupService.roundupForWeek(roundupRequest);
    }
    
}
