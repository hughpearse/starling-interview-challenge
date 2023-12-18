package com.starling.challenge.exceptions;

import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StarlingRuntimeException extends RuntimeException {
    private final HttpStatusCode statusCode;
    private final HttpHeaders headers;
    private final InputStream body;
}
