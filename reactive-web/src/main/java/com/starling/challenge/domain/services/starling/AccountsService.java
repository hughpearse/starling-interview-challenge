package com.starling.challenge.domain.services.starling;

import java.math.BigInteger;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.domain.model.starling.ConfirmationOfFundsResponse;
import com.starling.challenge.outboundclients.starling.AccountsClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Account Domain Service
 */
@Service
@Slf4j
public class AccountsService implements AccountsServiceInt {

    private AccountsClient accountsClient;

    public AccountsService(
        AccountsClient accountsClient
    ){
        this.accountsClient = accountsClient;
    }

    public Mono<AccountV2> getAccount(String accountName) {
        return accountsClient.getAccounts()
            .flatMapIterable(Accounts::getAccounts)
            .filter(account -> account.getName().equals(accountName))
            .next()
            .doOnNext(account -> log.info("Account found."))
            .switchIfEmpty(Mono.defer(() -> {
                log.info("No account found.");
                return Mono.empty();
            }));
    }

    public Mono<ConfirmationOfFundsResponse> getConfirmationOfFunds(
        UUID accountUid, 
        BigInteger targetAmountInMinorUnits
        ) {
        return accountsClient.getConfirmationOfFunds(accountUid, targetAmountInMinorUnits);
    }
    
}
