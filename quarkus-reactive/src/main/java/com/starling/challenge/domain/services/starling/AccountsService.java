package com.starling.challenge.domain.services.starling;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import com.starling.challenge.config.MicroProfileConfig;
import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;
import com.starling.challenge.outboundclients.starling.AccountsClient;
import com.starling.challenge.outboundclients.starling.BaseHttpClient;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

/**
 * Account Domain Service
 */
@ApplicationScoped
@Slf4j
public class AccountsService implements AccountsServiceInt {

    private final AccountsClient accountsClient;

    public AccountsService(MicroProfileConfig microProfileConfig) {
        this.accountsClient = new BaseHttpClient<>(microProfileConfig.domain(), AccountsClient.class).getService();
    }

    public Uni<AccountV2> getAccount(String accountName) {
        Uni<Accounts> accountsUni = accountsClient.getAccounts();
        Uni<List<AccountV2>> listUni = accountsUni.map(accounts -> accounts.getAccounts());
        Uni<AccountV2> accountV2Uni = listUni.flatMap(list -> {
            AccountV2 accountV2 = list.stream().filter(account -> account.getName().equals(accountName)).findFirst().orElse(null);
            return Uni.createFrom().item(accountV2);
        });
        accountV2Uni = accountV2Uni.map(account -> {
            if (account != null) {
                log.info("Account found.");
            } else {
                log.info("No account found.");
            }
            return account;
        });
        return accountV2Uni;
    }

    public Uni<ConfirmationOfFundsResponse> getConfirmationOfFunds(
        UUID accountUid, 
        BigInteger targetAmountInMinorUnits
        ) {
        return accountsClient.getConfirmationOfFunds(accountUid, targetAmountInMinorUnits);
    }
    
}
