package com.starling.challenge.exceptions;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StarlingRuntimeException extends RuntimeException {
    private final WebClientResponseException response;
}
