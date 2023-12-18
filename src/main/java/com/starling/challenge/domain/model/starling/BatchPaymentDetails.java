package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The details of the batch payment this is part of, if it is
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchPaymentDetails {
    private UUID batchPaymentUid;
    private String batchPaymentType;
}
