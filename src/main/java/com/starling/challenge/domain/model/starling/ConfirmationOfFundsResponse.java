package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Undocumented
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationOfFundsResponse {
    private boolean requestedAmountAvailableToSpend;
    private boolean accountWouldBeInOverdraftIfRequestedAmountSpent;
}
