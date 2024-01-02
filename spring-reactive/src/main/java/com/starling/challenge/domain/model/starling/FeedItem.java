package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * An item from the account holders's transaction feed
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedItem {
    public enum Direction {
        IN, OUT
    }

    private UUID feedItemUid;
    private UUID categoryUid;
    private CurrencyAndAmount amount;
    private CurrencyAndAmount sourceAmount;
    private Direction direction;
    private String updatedAt;
    private String transactionTime;
    private String settlementTime;
    private String retryAllocationUntilTime;
    private String source;
    private String sourceSubType;
    private String status;
    private UUID transactingApplicationUserUid;
    private String counterPartyType;
    private UUID counterPartyUid;
    private String counterPartyName;
    private UUID counterPartySubEntityUid;
    private String counterPartySubEntityName;
    private String counterPartySubEntityIdentifier;
    private String counterPartySubEntitySubIdentifier;
    private double exchangeRate;
    private int totalFees;
    private CurrencyAndAmount totalFeeAmount;
    private String reference;
    private String country;
    private String spendingCategory;
    private String userNote;
    private AssociatedFeedRoundUp roundUp;
    private boolean hasAttachment;
    private boolean hasReceipt;
    private BatchPaymentDetails batchPaymentDetails;
}
