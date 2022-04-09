package com.jolles.restfulservice.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(Long id) {
        super("Could not find object " + id);
    }
}
