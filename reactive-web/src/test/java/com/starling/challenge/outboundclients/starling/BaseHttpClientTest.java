package com.starling.challenge.outboundclients.starling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseHttpClientTest {

    @Mock
    private BaseHttpClient baseHttpClient;

    @Mock
    private RestClient restClient;

    @Test
    public void getClientTest() {
        // Arrange: configure mock responses
        WebClient mockWebClient = mock(WebClient.class);
        when(baseHttpClient.getClient()).thenReturn(mockWebClient);

        // Act: perform the test
        WebClient result = baseHttpClient.getClient();

        // Assert: check results
        assertNotNull(result);
    }

}
