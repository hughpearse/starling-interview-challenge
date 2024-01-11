package com.starling.challenge.outboundclients.starling;

import java.math.BigInteger;
import java.util.UUID;

import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;

public interface AccountsClient {
    /**
     * Get all accounts.
     * @return Accounts object
     */
    public Accounts getAccounts();

    /**
     * Check user has funds to transfer.
     * @param accountUid account UUID
     * @param targetAmountInMinorUnits amount to transfer
     * @return ConfirmationOfFundsResponse object
     */
    public ConfirmationOfFundsResponse getConfirmationOfFunds(UUID accountUid, BigInteger targetAmountInMinorUnits);
}
