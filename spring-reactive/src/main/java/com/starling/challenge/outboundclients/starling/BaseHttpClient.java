package com.starling.challenge.outboundclients.starling;

import org.springframework.web.reactive.function.client.WebClient;

public interface BaseHttpClient {
    /**
     * HTTP client for outbound requests
     * @return HTTP client builder
     */
    public WebClient getClient();
}
