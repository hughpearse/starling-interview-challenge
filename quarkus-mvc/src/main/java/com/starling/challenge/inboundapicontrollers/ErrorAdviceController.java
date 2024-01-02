package com.starling.challenge.inboundapicontrollers;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import com.starling.challenge.domain.model.starling.ErrorDetail;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ErrorAdviceController implements ResponseExceptionMapper<RuntimeException>, ExceptionMapper<Exception> {

    /*
     * Handle remote starling errors
     */
    @Override
    public RuntimeException toThrowable(Response response) {
        if (response.getStatus() >= 300) {
            throw new RuntimeException("The remote service responded with HTTP " + response.getStatus());
        }
        return null;
    }

    /**
     * Handle internal server errors
     */
    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorDetail(e.getMessage()))
                .build();
    }
}