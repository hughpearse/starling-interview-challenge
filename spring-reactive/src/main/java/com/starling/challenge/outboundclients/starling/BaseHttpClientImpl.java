package com.starling.challenge.outboundclients.starling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for authenticating and interacting with starling base hostname.
 */
@Configuration
public class BaseHttpClientImpl implements BaseHttpClient {
    
    private final WebClient webClient;

    /**
     * Constructor for Starling base auth HTTP client.
     * @param starlingbaseurl injected base hostname
     * @param accesstoken injected access token for Bearer auth
     */
    public BaseHttpClientImpl(
        @Value("${outboundclients.starling.core.baseurl}")
        String starlingbaseurl,
        @Value("${outboundclients.starling.core.accesstoken}")
        char[] accesstoken
    ){
        this.webClient = WebClient.builder()
        .baseUrl(starlingbaseurl)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + String.valueOf(accesstoken))
        .defaultHeader(HttpHeaders.USER_AGENT, "Hugh Pearse")
        .build();
    }
    
    public WebClient getClient() {
        return webClient;
    }
}