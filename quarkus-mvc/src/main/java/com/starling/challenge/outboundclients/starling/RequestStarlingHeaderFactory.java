package com.starling.challenge.outboundclients.starling;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import com.starling.challenge.config.MicroProfileConfig;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

@ApplicationScoped
public class RequestStarlingHeaderFactory implements ClientHeadersFactory {

    private final MicroProfileConfig microProfileConfig;

    public RequestStarlingHeaderFactory(MicroProfileConfig microProfileConfig) {
        this.microProfileConfig = microProfileConfig;
    }

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientOutgoingHeaders) {
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add(jakarta.ws.rs.core.HttpHeaders.USER_AGENT, microProfileConfig.useragent());
        result.add(jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION, microProfileConfig.authorization());
        return result;
    }
}
