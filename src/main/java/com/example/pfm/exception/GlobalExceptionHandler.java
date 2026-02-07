package com.example.pfm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(MethodArgumentNotValidException.class)
     public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
          Map<String, String> errors = new HashMap<>();
          ex.getBindingResult().getAllErrors().forEach((error) -> {
               String fieldName = ((FieldError) error).getField();
               String errorMessage = error.getDefaultMessage();
               errors.put(fieldName, errorMessage);
          });
          return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
     }

     @ExceptionHandler(IllegalArgumentException.class)
     public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
          return new ResponseEntity<>(Collections.singletonMap("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
     }

     // You might want to create custom exceptions for NotFound and Conflict to be
     // cleaner,
     // but for now I will rely on the messages or generic RuntimeException if
     // controllers rethrow.
     // However, since controllers currently catch exceptions, this handler receives
     // what bubbles up.

     @ExceptionHandler(Exception.class)
     public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
          return new ResponseEntity<>(Collections.singletonMap("message", getRootCause(ex).getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
     }

     private Throwable getRootCause(Throwable t) {
          Throwable result = t;
          while (result.getCause() != null) {
               result = result.getCause();
          }
          return result;
     }
}
