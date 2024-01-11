package com.starling.challenge.outboundclients.starling;

import org.springframework.web.client.RestClient;

public interface BaseHttpClient {
    /**
     * HTTP client for outbound requests
     * @return HTTP client builder
     */
    public RestClient getClient();
}
