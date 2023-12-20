package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;
import java.util.UUID;

/**
 * Bank account details
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountV2 {
    private UUID accountUid;
    private String accountType;
    private String defaultCategory;
    private Currency currency;
    private String createdAt;
    private String name;   
}
