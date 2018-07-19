package com.neotech.controller.exception;

import com.neotech.util.ValidationResponse;

public class ValidationException extends RuntimeException{

    private ValidationResponse response;

    public ValidationException(ValidationResponse response) {
        this();
        this.response = response;
    }

    public ValidationException() {
        super("Validation has failed");
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationResponse getResponse() {
        return response;
    }

    public void setResponse(ValidationResponse response) {
        this.response = response;
    }
}
