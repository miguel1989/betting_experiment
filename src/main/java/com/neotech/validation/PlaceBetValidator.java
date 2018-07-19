package com.neotech.validation;

import com.neotech.dao.BetDao;
import com.neotech.domain.Bet;
import com.neotech.domain.PlaceBetDTO;
import com.neotech.domain.Setting;
import com.neotech.service.SettingService;
import com.neotech.util.Constants;
import com.neotech.util.ValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PlaceBetValidator extends BaseValidator {

    @Autowired
    BetDao betDao;
    @Autowired
    SettingService settingService;

    public Bet validate(PlaceBetDTO placeBetDTO) {
        ValidationResponse response = new ValidationResponse();

        hibernateValidate(response, placeBetDTO);
        if (placeBetDTO.getAmount() != null) {
            hibernateValidate(response, placeBetDTO.getAmount());
        }
        response.finishValidation();

        Bet oldBet = betDao.findOne(placeBetDTO.getBetId());

        if (oldBet == null){
            response.addError(Constants.ERR_NOT_VALID_BET);
            response.finishValidation();
        }

        if (placeBetDTO.getTimestamp() != oldBet.getTimestamp()) {
            response.addError(Constants.ERR_BET_CHANGED);
            response.finishValidation();
        }

        this.validateOdd(placeBetDTO, oldBet, response);

        this.validateMinAndMaxAmount(placeBetDTO, response);

        return oldBet;
    }

    private void validateOdd(PlaceBetDTO placeBetDTO, Bet oldBet, ValidationResponse response) {
        switch (placeBetDTO.getType()) {
            case WIN:
                if (placeBetDTO.getOdd() != oldBet.getOdd().getWin()) {
                    response.addError(Constants.ERR_ODD_MISMATCH);
                }
                break;
            case DRAW:
                if (placeBetDTO.getOdd() != oldBet.getOdd().getDraw()) {
                    response.addError(Constants.ERR_ODD_MISMATCH);
                }
                break;
            case LOSE:
                if (placeBetDTO.getOdd() != oldBet.getOdd().getLose()) {
                    response.addError(Constants.ERR_ODD_MISMATCH);
                }
                break;
        }
        response.finishValidation();
    }

    private void validateMinAndMaxAmount(PlaceBetDTO placeBetDTO, ValidationResponse response) {
        //Assumption that this props are always there. no fallback to default values
        Setting setting = settingService.findDefault();
        BigDecimal minBetAmount = new BigDecimal((String)setting.getValues().get(Constants.SETTING_MIN_BET_AMOUNT));
        BigDecimal maxBetAmount = new BigDecimal((String)setting.getValues().get(Constants.SETTING_MAX_BET_AMOUNT));

        int minValCompareResult = placeBetDTO.getAmount().getValue().compareTo(minBetAmount);
        if (minValCompareResult == -1) { //less than min value
            response.addFieldError("value", Constants.ERR_BET_AMOUNT_MIN_VALUE, new String[]{minBetAmount.setScale(Constants.DECIMAL_SCALE, RoundingMode.HALF_UP).toString()});
        }
        //#todo maybe use Range? instead of min and max

        BigDecimal oddBigDecimal = new BigDecimal(placeBetDTO.getOdd());
        BigDecimal dynamicMaxBetVal = maxBetAmount.divide(oddBigDecimal, Constants.DECIMAL_SCALE, RoundingMode.HALF_UP);
        int maxValCompareResult = placeBetDTO.getAmount().getValue().compareTo(dynamicMaxBetVal);
        if (maxValCompareResult == 1) { //more than max value
            response.addFieldError("value", Constants.ERR_BET_AMOUNT_MAX_VALUE, new String[]{dynamicMaxBetVal.setScale(Constants.DECIMAL_SCALE, RoundingMode.HALF_UP).toString()});
        }
        response.finishValidation();
    }
}
