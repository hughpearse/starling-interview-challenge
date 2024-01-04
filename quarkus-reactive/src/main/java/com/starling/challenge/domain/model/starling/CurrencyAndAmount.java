package com.starling.challenge.domain.model.starling;

import java.math.BigInteger;
import java.util.Currency;

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
    private Currency currency;
    private BigInteger minorUnits;
}
