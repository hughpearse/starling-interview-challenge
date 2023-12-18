package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * A goal defined by an account holder to hold savings
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsGoalV2 {
    private UUID savingsGoalUid;
    private String name;
    private CurrencyAndAmount target;
    private CurrencyAndAmount totalSaved;
    private int savedPercentage;
    private String state;
}
