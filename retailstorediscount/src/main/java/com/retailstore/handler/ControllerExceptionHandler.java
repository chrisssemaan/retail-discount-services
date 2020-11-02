package com.retailstore.handler;

import com.retailstore.dtos.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice(basePackages = "com.retailstore.controller")
public class ControllerExceptionHandler {

    /**
     * Not found exception handler error message.
     *
     * @param ex the ex
     * @return the error message
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ApiError notFoundExceptionHandler(EntityNotFoundException ex)
    {
        return new ApiError(String.valueOf(HttpStatus.NOT_FOUND.value())
                ,HttpStatus.NOT_FOUND.getReasonPhrase()
                ,ex.getMessage());
    }
}
