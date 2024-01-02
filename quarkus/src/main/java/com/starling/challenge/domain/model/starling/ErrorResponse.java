package com.starling.challenge.domain.model.starling;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Erroneous response wrapper
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private List<ErrorDetail> errors;
    private Boolean success;
}
