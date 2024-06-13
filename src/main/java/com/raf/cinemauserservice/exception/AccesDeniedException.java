package com.raf.cinemauserservice.exception;

import org.springframework.http.HttpStatus;

public class AccesDeniedException extends CustomException{
    public AccesDeniedException(String message) {
        super(message, ErrorCode.ACCESS_DENIED, HttpStatus.FORBIDDEN);
    }
}
