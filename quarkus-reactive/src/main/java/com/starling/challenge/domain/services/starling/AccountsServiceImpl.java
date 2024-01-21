package com.starling.challenge.domain.services.starling;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import com.starling.challenge.config.MicroProfileConfig;
import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;
import com.starling.challenge.outboundclients.starling.AccountsClient;
import com.starling.challenge.outboundclients.starling.BaseHttpClientImpl;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
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
        this.accountsClient = new BaseHttpClientImpl<>(microProfileConfig.domain(), AccountsClient.class).getService();
    }

    public Uni<AccountV2> getAccount(String accountName) {
        log.info("Getting account");
        Uni<Accounts> accountsUni = accountsClient.getAccounts();
        Uni<List<AccountV2>> listUni = accountsUni.onItem().transform(accounts -> accounts.getAccounts());
        return listUni.onItem().transformToMulti(Multi.createFrom()::iterable)
            .filter(account -> accountName.equals(account.getName()))
            .toUni()
            .onItem().ifNotNull().invoke(response -> log.info("Account found."))
            .onFailure().invoke(t -> log.info("Account not found."));
    }

    public Uni<ConfirmationOfFundsResponse> getConfirmationOfFunds(
        UUID accountUid, 
        BigInteger targetAmountInMinorUnits
        ) {
        log.info("Getting confirmation of funds");
        return accountsClient.getConfirmationOfFunds(accountUid, targetAmountInMinorUnits)
        .onItem().ifNotNull().invoke(response -> log.info("Funds confirmed."))
        .onFailure().invoke(t -> log.info("Funds not confirmed."));
    }
}
