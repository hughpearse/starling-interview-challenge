package com.starling.challenge.outboundclients.starling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

/**
 * Service for authenticating and interacting with starling base hostname.
 */
@Configuration
public class BaseHttpClientImpl implements BaseHttpClient {
    
	private final RestClient restClient;

    /**
     * Constructor for Starling base auth HTTP client.
     * @param starlingbaseurl injected base hostname
     * @param accesstoken injected access token for Bearer auth
     * @param restClientBuilder injected HTTP client object
     */
    public BaseHttpClientImpl(
        @Value("${outboundclients.starling.core.baseurl}")
        String starlingbaseurl,
        @Value("${outboundclients.starling.core.accesstoken}")
        char[] accesstoken,
        RestClient.Builder restClientBuilder
    ){
        this.restClient = restClientBuilder
        .baseUrl(starlingbaseurl)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + String.valueOf(accesstoken))
        .defaultHeader(HttpHeaders.USER_AGENT, "Hugh Pearse")
        .build();
    }
    
    public RestClient getClient() {
        return restClient;
    }
}
