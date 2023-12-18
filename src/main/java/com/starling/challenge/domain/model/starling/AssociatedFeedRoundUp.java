package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Round up details associated with a feed item
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedFeedRoundUp {
    private UUID goalCategoryUid;
    private CurrencyAndAmount amount;
}
