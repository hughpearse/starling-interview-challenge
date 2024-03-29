package com.starling.challenge.domain.services.challenge;


import com.starling.challenge.domain.model.challenge.RoundupRequest;
import com.starling.challenge.domain.model.challenge.RoundupResponse;

import io.smallrye.mutiny.Uni;

public interface RoundupService {
    /**
     * Allows user to select a week in the past and retroactively round up their transactions into a savings goal.
     * @param roundupRequest the request as a RoundupRequest object
     * @return the amount rounded up as ResponseEntity object
     */
    public Uni<RoundupResponse> roundupForWeek(RoundupRequest roundupRequest);
}
