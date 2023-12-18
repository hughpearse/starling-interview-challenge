package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to create a new savings goal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsGoalRequestV2 {
    private String name;
    private String currency;
    private CurrencyAndAmount target;
    private String base64EncodedPhoto;
}
