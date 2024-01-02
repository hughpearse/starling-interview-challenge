package com.starling.challenge.exceptions;

import org.springframework.http.client.ClientHttpResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StarlingRuntimeException extends RuntimeException {
    private final ClientHttpResponse response;
}
