package com.neotech.controller.exception;

import com.neotech.util.BaseResponse;
import com.neotech.util.Constants;
import com.neotech.util.CoreUtils;
import com.neotech.util.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ExceptionsController implements ErrorController {
    private static final String PATH = "/error";
    private static final Logger logger = LoggerFactory.getLogger(ExceptionsController.class);

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleFatalException(Throwable throwable) {
        logger.debug("Unhandled exception: ", throwable);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(Constants.ERR_UNHANDLED_EXCEPTION);
        baseResponse.setMessage(throwable.getMessage());
        baseResponse.setDebugInfo(CoreUtils.stackTraceToString(throwable));
        return baseResponse;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Object handleAccessDeniedException(AccessDeniedException e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(Constants.ERR_ACCESS_DENIED);
        baseResponse.setMessage(e.getMessage());
        return baseResponse;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Object handleMethodNotAllowed(Exception e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(Constants.ERR_METHOD_NOT_ALLOWED);
        baseResponse.setMessage(e.getMessage());
        return baseResponse;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleResourceNotFoundException(Exception e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(Constants.ERR_RESOURCE_NOT_FOUND);
        baseResponse.setMessage(e.getMessage());
        return baseResponse;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleValidationException(ValidationException e) {
        if (e.getResponse() != null) {
            ValidationResponse response = e.getResponse();
            response.setMessage(e.getMessage());
            return response;
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(Constants.ERR_VALIDATION_FAILED);
        baseResponse.setMessage(e.getMessage());
        return baseResponse;
    }
}
