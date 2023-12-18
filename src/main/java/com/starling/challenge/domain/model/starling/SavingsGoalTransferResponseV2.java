package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Response after attempting to make an immediate or recurring transfer into/out of a savings goal.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsGoalTransferResponseV2 {
    private UUID transferUid;
    private boolean success;
}
