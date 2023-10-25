package com.fidelity.india.secondary.assessment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.ServerWebInputException;

/**
 * GlobalExceptionHandler configures Spring Boot so that when a web service method
 * throws a ServerWebInputException or ServerErrorException, the response body is 
 * completely empty. By default, Spring Boot populates the response body with a 
 * JSON object with exception info, which may cause the client's JSON deserializer 
 * to throw an exception.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<Void> handleServerWebInputException(ServerWebInputException ex) {
        // Return an empty response body with the status code 400
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<Void> handleServerErrorException(ServerErrorException ex) {
        // Return an empty response body with the status code 500
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}