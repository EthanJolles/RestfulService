package com.jolles.restfulservice.controller;

import com.jolles.restfulservice.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ObjectNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String objectNotFoundHandler(ObjectNotFoundException ex) {
        return ex.getMessage();
    }
}
