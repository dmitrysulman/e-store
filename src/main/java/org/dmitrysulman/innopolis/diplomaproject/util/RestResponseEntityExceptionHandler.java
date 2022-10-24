package org.dmitrysulman.innopolis.diplomaproject.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.Instant;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(status.getReasonPhrase(), Instant.now(), status.value());
        for (FieldError fieldError : ex.getFieldErrors()) {
            errorResponse.addValidationError(fieldError);
        }
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpHeaders headers = new HttpHeaders();
        ErrorResponse errorResponse = new ErrorResponse(status.getReasonPhrase(), Instant.now(), status.value());
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
//            violation.
            FieldError fieldError = new FieldError(violation.getRootBeanClass().getName(),
                    violation.getPropertyPath().toString(),
                    violation.getMessage());
            errorResponse.addValidationError(fieldError);
        }
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }
}
