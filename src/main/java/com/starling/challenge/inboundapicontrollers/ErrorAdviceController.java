package com.starling.challenge.inboundapicontrollers;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starling.challenge.domain.model.starling.ErrorDetail;
import com.starling.challenge.domain.model.starling.ErrorResponse;
import com.starling.challenge.exceptions.StarlingRuntimeException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ErrorAdviceController {
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handle client errors
     * @param ex the HttpMessageNotReadableException object
     * @return ResponseEntity<ErrorResponse> object
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
    HttpMessageNotReadableException ex) {
        log.error("Client error: " + ex.getMessage());
        ErrorResponse errors = new ErrorResponse();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setMessage(ex.getMessage());
        errors.setErrors(Arrays.asList(errorDetail));
        errors.setSuccess(false);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle backend Starling errors
     * @param ex the StarlingRuntimeException object
     * @return the ResponseEntity<ErrorResponse> object
     */
    @ExceptionHandler(StarlingRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUpstreamExceptions(
    StarlingRuntimeException ex
    ) {
        ResponseEntity<ErrorResponse> responseEntity = null;
        try {
            ErrorResponse errors = objectMapper.readValue(ex.getResponse().getBody(), ErrorResponse.class);
            responseEntity = new ResponseEntity<>(errors, ex.getResponse().getStatusCode());
        } catch (Exception e) {
            log.error("Backend error: " + e.getMessage());
        }
        return responseEntity;
    }

    /**
     * Handle internel server errors
     * @param ex the StarlingRuntimeException object
     * @return the ResponseEntity<ErrorResponse> object
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalExceptions(
    Exception ex
    ) {
        log.error("Internal error: " + ex.getMessage());
        ErrorResponse errors = new ErrorResponse();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setMessage(ex.getMessage());
        errors.setErrors(Arrays.asList(errorDetail));
        errors.setSuccess(false);
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
