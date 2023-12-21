package com.starling.challenge.domain.model.starling;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A list containing all savings goals for account holder
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsGoalsV2 {
    private List<SavingsGoalV2> savingsGoalList;
}
