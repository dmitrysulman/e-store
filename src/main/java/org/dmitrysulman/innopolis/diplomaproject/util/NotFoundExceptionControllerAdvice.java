package org.dmitrysulman.innopolis.diplomaproject.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class NotFoundExceptionControllerAdvice {
    @ExceptionHandler({ElementNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleElementNotFound(ElementNotFoundException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), Instant.now(), status.value());

        return new ResponseEntity<>(errorResponse, status);
    }
}
