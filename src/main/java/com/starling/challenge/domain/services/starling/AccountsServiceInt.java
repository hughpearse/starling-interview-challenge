package com.starling.challenge.domain.services.starling;

import java.math.BigInteger;
import java.util.UUID;

import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;

/**
 * Account Domain Service
 */
public interface AccountsServiceInt {

    /**
     * Get account
     * @param accountName given an account name as input
     * @return a AccountV2 object
     */
    public AccountV2 getAccount(String accountName);

    /**
     * Confirm funds present before transfer
     * @param accountUid the account UUID
     * @param targetAmountInMinorUnits amount to transfer
     * @return the ConfirmationOfFundsResponse object
     */
    public ConfirmationOfFundsResponse getConfirmationOfFunds( UUID accountUid, BigInteger targetAmountInMinorUnits);
}
