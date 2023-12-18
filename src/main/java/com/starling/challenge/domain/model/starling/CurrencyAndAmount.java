package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representation of money
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyAndAmount {
    private String currency;
    private int minorUnits;    
}
