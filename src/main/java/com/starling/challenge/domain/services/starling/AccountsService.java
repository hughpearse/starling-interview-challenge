package com.starling.challenge.domain.services.starling;

import org.springframework.stereotype.Service;

import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.outboundclients.starling.AccountsClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Account Domain Service
 */
@Service
@Slf4j
public class AccountsService implements AccountsServiceInt {

    private AccountsClient accountsClient;

    /**
     * Constructor for Account domain service.
     * @param accountsClient injected HTTP client for interacting with accounts.
     */
    public AccountsService(
        AccountsClient accountsClient
    ){
        this.accountsClient = accountsClient;
    }

    public AccountV2 getAccount(String accountName) {
        Accounts accounts = accountsClient.getAccounts();
        AccountV2 accountFound = null;
        for(AccountV2 account : accounts.getAccounts()){
            if(account.getName().equals(accountName)){
                accountFound = account;
                log.info("Account found.");
                break;
            }
        }
        return accountFound;
    }
    
}
