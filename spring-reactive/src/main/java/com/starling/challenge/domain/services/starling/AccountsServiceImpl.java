package com.starling.challenge.domain.services.starling;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;
import com.starling.challenge.outboundclients.starling.AccountsClient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Account Domain Service
 */
@Service
@Slf4j
@AllArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private AccountsClient accountsClient;

    public Mono<AccountV2> getAccount(String accountName) {
        log.info("Getting account");
        Mono<Accounts> accountsMono = accountsClient.getAccounts();
        Mono<List<AccountV2>> listMono = accountsMono.map(accounts -> accounts.getAccounts());
        return listMono.flatMapIterable(accounts -> accounts)
        .filter(account -> accountName.equals(account.getName()))
        .next()
        .doOnSuccess(response -> log.info("Account found."))
        .doOnError(t -> log.info("Account not found."));
    }

    public Mono<ConfirmationOfFundsResponse> getConfirmationOfFunds(
        UUID accountUid, 
        BigInteger targetAmountInMinorUnits
        ) {
        log.info("Getting confirmation of funds");
        return accountsClient.getConfirmationOfFunds(accountUid, targetAmountInMinorUnits)
        .doOnSuccess(response -> log.info("Funds confirmed."))
        .doOnError(t -> log.info("Funds not confirmed."));
    }
    
}
