package com.neotech.validation;

import com.google.common.base.Strings;
import com.neotech.util.Constants;
import com.neotech.util.ValidationResponse;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class BaseValidator {
    public static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    public static final Validator validator = factory.getValidator();

    /**
     * Runs hibernate validator that processes field annotations in object
     *
     * @param response
     * @param  object to validate
     * @param <T>
     * @return
     */
    <T> Set<ConstraintViolation<T>> hibernateValidate(ValidationResponse response, T object) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            switch (constraintViolation.getMessage()) {
                case Constants.ERR_MAX_LENGTH: {
                    String[] values =
                            new String[] {constraintViolation.getConstraintDescriptor().getAttributes().get("max").toString()};
                    response.addFieldError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage(), values);
                    break;
                }
                case Constants.ERR_MIN_LENGTH: {
                    String[] values =
                            new String[] {constraintViolation.getConstraintDescriptor().getAttributes().get("value").toString()};
                    response.addFieldError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage(), values);
                    break;
                }
                default: {
                    response.addFieldError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                    break;
                }
            }
        }
        return constraintViolations;
    }

    public void validate(Object object) {
        ValidationResponse response = new ValidationResponse();
        hibernateValidate(response, object);
        response.finishValidation();
    }

    public void validateRange(String value, String fieldName, int min, int max, ValidationResponse response) {
        if (Strings.isNullOrEmpty(value)) {
            response.addFieldError(fieldName, Constants.ERR_REQUIRED);
        } else if (value.length() < min
                || value.length() > max) {
            response.addFieldError(fieldName,
                    Constants.ERR_RANGE,
                    new String[]{
                            Integer.toString(min),
                            Integer.toString(max)
                    });
        }
    }
}
