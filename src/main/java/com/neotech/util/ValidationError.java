package com.neotech.util;

import java.util.Arrays;

public class ValidationError {
    private String errorCode;

    private String[] values;

    public ValidationError() {
    }

    public ValidationError(String errorCode) {
        this.errorCode = errorCode;
    }

    public ValidationError(String errorCode, String[] values) {
        this.errorCode = errorCode;
        this.values = values;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "errorCode='" + errorCode + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
