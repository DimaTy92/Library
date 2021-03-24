package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AlreadyReservedException extends RuntimeException {

    public AlreadyReservedException(String message) {
        super(message);
    }
}