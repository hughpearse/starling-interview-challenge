package com.starling.challenge.domain.model.challenge;

import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Interview challenge custom object for inbound API response to round up money for a given week
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoundupResponse {
    SavingsGoalTransferResponseV2 transferToSavingsGoal;
    CurrencyAndAmount currencyAndAmount;
}
