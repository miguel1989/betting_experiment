package com.neotech.validation;

import com.neotech.domain.Bet;
import com.neotech.util.ValidationResponse;
import org.springframework.stereotype.Component;

@Component
public class BetValidator extends BaseValidator {

    public void validate(Bet bet) {
        ValidationResponse response = new ValidationResponse();
        hibernateValidate(response, bet);
        if (bet.getOdd() != null) {
            hibernateValidate(response, bet.getOdd());
        }
        response.finishValidation();
    }
}
