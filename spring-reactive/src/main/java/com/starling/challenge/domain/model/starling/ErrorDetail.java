package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail of the error
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetail {
    private String message;
}
