package com.starling.challenge.inboundapicontrollers;

import java.util.Arrays;

import com.starling.challenge.domain.model.starling.ErrorDetail;
import com.starling.challenge.domain.model.starling.ErrorResponse;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class ErrorAdviceController implements ExceptionMapper<Exception> {

    /**
     * Handle internal server errors
     */
    @Override
    public Response toResponse(Exception e) {
        ErrorResponse errors = new ErrorResponse();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setMessage(e.getMessage());
        errors.setErrors(Arrays.asList(errorDetail));
        errors.setSuccess(false);
        log.error("Internal error: " + errorDetail.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errors)
                .build();
    }
}
