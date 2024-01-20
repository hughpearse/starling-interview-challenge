package com.starling.challenge.inboundapicontrollers.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.starling.challenge.domain.model.challenge.RoundupRequest;
import com.starling.challenge.domain.services.challenge.RoundupServiceInt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Router for directing HTTP requests to the appropriate logic.
 */
@RestController
@AllArgsConstructor
public class RetroactiveRoundupController {
    private final RoundupServiceInt roundupService;

    /**
     * Allows user to select a week in the past and retroactively round up their transactions into a savings goal.
     * @param roundupRequest the request as a RoundupRequest object
     * @return the amount rounded up as ResponseEntity object
     */
    @Operation(summary = "Round up for week", description = "This operation rounds up for the week.")
    @ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "Successful operation"),
    @ApiResponse(responseCode = "400", description = "Invalid input"),
    @ApiResponse(responseCode = "500", description = "Internal server error") 
    })
    @RequestMapping(value = "/v1/retroactive-roundup", method = RequestMethod.POST)
    public ResponseEntity<?> roundUpForWeek(
        @Valid
        @RequestBody
        RoundupRequest roundupRequest
    ){
        return roundupService.roundupForWeek(roundupRequest);
    }
}
