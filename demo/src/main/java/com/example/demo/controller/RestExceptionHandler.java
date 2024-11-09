package com.example.demo.controller;

import org.library.exception.IllegalParameterException;
import org.library.exception.ResourceNotFoundException;
import org.library.dto.response.ApiErrorResponse;
import org.library.dto.response.ResourceNotFoundErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ResourceNotFoundErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new ResourceNotFoundErrorResponse(
                        "404",
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({IllegalParameterException.class})
    public ResponseEntity<ApiErrorResponse> handleException(IllegalParameterException ex) {
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        "Введны некорректные данные",
                        "400",
                        "IllegalParameterException",
                        ex.getMessage()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
