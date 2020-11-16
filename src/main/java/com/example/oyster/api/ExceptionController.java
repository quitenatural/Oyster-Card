package com.example.oyster.api;

import com.example.oyster.api.response.ExceptionResponse;
import com.example.oyster.exception.FareException;
import com.example.oyster.exception.JourneyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.xml.bind.ValidationException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity handleValidationException(ValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler({JourneyException.class})
    public ResponseEntity handleJourneyException(JourneyException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler({FareException.class})
    public ResponseEntity handleFareException(FareException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity handleRuntimeException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
