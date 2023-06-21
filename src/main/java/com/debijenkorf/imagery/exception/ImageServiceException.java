package com.debijenkorf.imagery.exception;

import lombok.Getter;

public class ImageServiceException extends RuntimeException {

    @Getter
    private final String errorCode;

    protected ImageServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
