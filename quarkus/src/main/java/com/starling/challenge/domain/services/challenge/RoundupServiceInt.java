package com.starling.challenge.domain.services.challenge;


import com.starling.challenge.domain.model.challenge.RoundupRequest;
import com.starling.challenge.domain.model.challenge.RoundupResponse;

public interface RoundupServiceInt {
    /**
     * Allows user to select a week in the past and retroactively round up their transactions into a savings goal.
     * @param roundupRequest the request as a RoundupRequest object
     * @return the amount rounded up as ResponseEntity object
     */
    public RoundupResponse roundupForWeek(RoundupRequest roundupRequest);
}
