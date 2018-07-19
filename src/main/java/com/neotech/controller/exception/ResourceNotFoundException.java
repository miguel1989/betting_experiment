package com.neotech.controller.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Resource with the given id not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
