package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to make an immediate transfer into a savings goal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopUpRequestV2 {
    private CurrencyAndAmount amount;
}
