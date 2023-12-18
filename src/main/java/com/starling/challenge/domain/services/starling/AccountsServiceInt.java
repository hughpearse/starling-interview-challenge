package com.starling.challenge.domain.services.starling;

import com.starling.challenge.domain.model.starling.AccountV2;

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
}
