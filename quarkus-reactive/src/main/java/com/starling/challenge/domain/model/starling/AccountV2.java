package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;
import java.util.Date;
import java.util.UUID;

/**
 * Bank account details
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountV2 {
    public enum AccountType {
        PRIMARY, ADDITIONAL, LOAN, FIXED_TERM_DEPOSIT
    }
    private UUID accountUid;
    private AccountType accountType;
    private UUID defaultCategory;
    private Currency currency;
    private Date createdAt;
    private String name;   
}
