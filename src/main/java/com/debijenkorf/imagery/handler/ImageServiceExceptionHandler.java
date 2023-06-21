package com.debijenkorf.imagery.handler;

import com.debijenkorf.imagery.dto.api.APIResponse;
import com.debijenkorf.imagery.dto.error.Error;
import com.debijenkorf.imagery.exception.ImageNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class ImageServiceExceptionHandler {

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse<?> handleServiceException(RuntimeException exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus("IMAGE NOT FOUND");
        serviceResponse.setErrors(Collections.singletonList(new Error("IMAGE_NOT_FOUND", exception.getMessage())));
        return serviceResponse;
    }

    @ExceptionHandler({ConstraintViolationException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public APIResponse<?> handleServiceException(Exception exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus("UNPROCESSABLE REQUEST");
        serviceResponse.setErrors(Collections.singletonList(new Error("UNPROCESSABLE REQUEST", exception.getMessage())));
        return serviceResponse;
    }

}
