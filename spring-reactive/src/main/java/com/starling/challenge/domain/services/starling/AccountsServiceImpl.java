package com.starling.challenge.domain.services.starling;

import java.math.BigInteger;
import java.util.List;
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
public class AccountsServiceImpl implements AccountsService {

    private AccountsClient accountsClient;

    public AccountsServiceImpl(
        AccountsClient accountsClient
    ){
        this.accountsClient = accountsClient;
    }

    public Mono<AccountV2> getAccount(String accountName) {
        Mono<Accounts> accountsMono = accountsClient.getAccounts();
        Mono<List<AccountV2>> listMono = accountsMono.map(accounts -> accounts.getAccounts());
        Mono<AccountV2> accountV2Mono = listMono.flatMap(list -> {
            AccountV2 accountV2 = list.stream().filter(account -> account.getName().equals(accountName)).findFirst().orElse(null);
            return Mono.just(accountV2);
        });
        accountV2Mono = accountV2Mono.map(account -> {
            if (account != null) {
                log.info("Account found.");
            } else {
                log.info("No account found.");
            }
            return account;
        });
        return accountV2Mono;
    }

    public Mono<ConfirmationOfFundsResponse> getConfirmationOfFunds(
        UUID accountUid, 
        BigInteger targetAmountInMinorUnits
        ) {
        return accountsClient.getConfirmationOfFunds(accountUid, targetAmountInMinorUnits);
    }
    
}
