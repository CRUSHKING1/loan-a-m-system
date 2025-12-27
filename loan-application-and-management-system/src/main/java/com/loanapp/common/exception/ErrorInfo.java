package com.loanapp.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

@Getter
@Setter

public class ErrorInfo {

    private final String message;
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public ErrorInfo(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
