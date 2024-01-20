package com.starling.challenge.domain.services.starling;

import java.math.BigInteger;
import java.util.UUID;

import com.starling.challenge.config.MicroProfileConfig;
import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;
import com.starling.challenge.outboundclients.starling.AccountsClient;
import com.starling.challenge.outboundclients.starling.BaseHttpClient;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

/**
 * Account Domain Service
 */
@ApplicationScoped
@Slf4j
public class AccountsServiceImpl implements AccountsService {

    private final AccountsClient accountsClient;

    public AccountsServiceImpl(MicroProfileConfig microProfileConfig) {
        this.accountsClient = new BaseHttpClient<>(microProfileConfig.domain(), AccountsClient.class).getService();
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

    public ConfirmationOfFundsResponse getConfirmationOfFunds(
        UUID accountUid, 
        BigInteger targetAmountInMinorUnits
        ) {
        return accountsClient.getConfirmationOfFunds(accountUid, targetAmountInMinorUnits);
    }
    
}
