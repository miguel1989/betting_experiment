package com.neotech.validation;

import com.google.common.base.Strings;
import com.neotech.dao.UserDao;
import com.neotech.domain.User;
import com.neotech.util.Constants;
import com.neotech.util.ValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidator extends BaseValidator {

    @Autowired
    UserDao userDao;

    public void validate(User user) {
        ValidationResponse response = new ValidationResponse();
        hibernateValidate(response, user);

        validateRange(user.getUsername(), "username", Constants.MIN_USER_NAME_LENGTH, Constants.MAX_USER_NAME_LENGTH, response);
        validateRange(user.getPassword(), "password", Constants.MIN_PASS_LENGTH, Constants.MAX_PASS_LENGTH, response);

        if (Strings.isNullOrEmpty(user.getEmail())) {
            response.addFieldError("email", Constants.ERR_REQUIRED);
        }
        response.finishValidation();

        User oldUser = userDao.findByUsername(user.getUsername());
        if (oldUser != null) {
            response.addError(Constants.ERR_DUPLICATE_USERNAME);
        }

        response.finishValidation();
    }
}
