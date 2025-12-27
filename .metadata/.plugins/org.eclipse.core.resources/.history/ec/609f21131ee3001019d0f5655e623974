package com.loanapp.common.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorInfo {

    private final String message;
    private final int status;
    private final LocalDateTime timestamp;

    public ErrorInfo(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
