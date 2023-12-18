package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Response after attempting to create a savings goal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateSavingsGoalResponseV2 {
    private UUID savingsGoalUid;
    private boolean success;
}
