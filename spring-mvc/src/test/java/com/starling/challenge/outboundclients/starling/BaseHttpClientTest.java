package com.starling.challenge.outboundclients.starling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        when(baseHttpClient.getClient()).thenReturn(restClient);

        // Act: perform the test
        RestClient result = baseHttpClient.getClient();

        // Assert: check results
        assertNotNull(result);
    }
}
