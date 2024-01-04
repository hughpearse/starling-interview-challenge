package com.starling.challenge.domain.model.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Interview challenge custom object for inbound API request to round up money for a given week
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoundupRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date weekStarting;
    private String goalName;
    private String accountname;
    @JsonIgnore
    private BigInteger optionalSavingsGoalTarget = new BigInteger("10000");
}
