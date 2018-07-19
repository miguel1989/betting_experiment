package com.neotech.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.neotech.controller.exception.ValidationException;
import java.util.List;
import java.util.Map;


public class ValidationResponse extends BaseResponse {

    private Map<String, List<ValidationError>> fieldErrors = Maps.newHashMap();
    private List<ValidationError> errors = Lists.newArrayList();

    public ValidationResponse() {
        super.code = Constants.ERR_VALIDATION_FAILED;
    }

    public ValidationResponse(String error) {
        this();
        addError(error);
    }

    public static void throwError(String errorId) {
        ValidationResponse response = new ValidationResponse();
        response.addError(errorId);
        throw new ValidationException(response);
    }

    public static void throwFieldError(String fieldName, String errorId) {
        ValidationResponse response = new ValidationResponse();
        response.addFieldError(fieldName, errorId);
        throw new ValidationException(response);
    }

    public void addFieldErrorRequired(String fieldName) {
        addFieldError(fieldName, Constants.ERR_REQUIRED);
    }

    public void addFieldError(String fieldName, String error) {
        addFieldError(fieldName, error, null);
    }

    public void addFieldError(String fieldName, String error, String[] values) {
        ValidationError validationError = new ValidationError(error, values);
        if (!fieldErrors.containsKey(fieldName)) {
            fieldErrors.put(fieldName, Lists.newArrayList());
        }
        fieldErrors.get(fieldName).add(validationError);
    }

    public void addError(String error) {
        addError(error, null);
    }

    public void addError(String error, String[] values) {
        ValidationError validationError = new ValidationError(error, values);
        errors.add(validationError);
    }

    public void finishValidation() {
        if (hasErrors()) {
            throw new ValidationException(this);
        }
    }

    public boolean hasErrors() {
        return !errors.isEmpty() || !fieldErrors.isEmpty();
    }

    public Map<String, List<ValidationError>> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, List<ValidationError>> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ValidationResponse{" +
                "fieldErrors=" + fieldErrors +
                ", errors=" + errors +
                '}';
    }
}
