package com.starling.challenge.outboundclients.starling;

import java.net.URI;

import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;

public class BaseHttpClient <T> {
    
    private final T service;

    public BaseHttpClient(URI domain, Class<T> serviceClass) {
        this.service = QuarkusRestClientBuilder.newBuilder()
                .baseUri(domain)
                .build(serviceClass);
    }

    public T getService() {
        return service;
    }
}
