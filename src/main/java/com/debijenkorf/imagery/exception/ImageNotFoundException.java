package com.debijenkorf.imagery.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageNotFoundException extends ImageServiceException {

    @Getter
    private final String errorCode;

    public ImageNotFoundException(String errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
        log.info("{}: {}:", this.errorCode, message);
    }

}