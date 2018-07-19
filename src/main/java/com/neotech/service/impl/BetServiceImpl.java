package com.neotech.service.impl;

import com.google.common.base.Strings;
import com.neotech.controller.exception.ResourceNotFoundException;
import com.neotech.dao.BetDao;
import com.neotech.domain.Bet;
import com.neotech.service.BetService;
import com.neotech.util.Constants;
import com.neotech.util.ValidationResponse;
import com.neotech.validation.BetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetServiceImpl implements BetService {

    @Autowired
    private BetDao betDao;
    @Autowired
    private BetValidator betValidator;

    @Override
    public List<Bet> findAll() {
        return betDao.findAll();
    }

    @Override
    public Bet findOne(String id) {
        return this._findOne(id);
    }

    @Override
    public Bet create(Bet bet) {
        bet.setId(null);//mongo should generate it's own
        betValidator.validate(bet);
        bet.updateTimeStamp();
        return betDao.save(bet);
    }

    @Override
    public Bet update(Bet bet) {
        betValidator.validate(bet);
        Bet oldBet = _findOne(bet.getId());
        //timestamp missmatch. probably some one already updated this bet
        //kind of concurrent update check
        if (bet.getTimestamp() != oldBet.getTimestamp()) {
            ValidationResponse.throwError(Constants.ERR_CONCURRENT_MODIFICATION);
        }

        oldBet.setName(bet.getName());
        oldBet.setOdd(bet.getOdd());
        oldBet.updateTimeStamp();
        return betDao.save(oldBet);
    }

    @Override
    public Bet deleteByTimestamp(long timestamp) {
        Bet bet = betDao.findByTimestamp(timestamp);
        if (bet == null) {
            throw new ResourceNotFoundException();
        }
        betDao.delete(bet.getId());
        return bet;
    }

    @Override
    public void deleteAll() {
        betDao.deleteAll();
    }

    private Bet _findOne(String id) {
        if (Strings.isNullOrEmpty(id)) {
            throw new ResourceNotFoundException();
        }
        Bet bet = betDao.findOne(id);
        if (bet == null){
            throw new ResourceNotFoundException();
        }
        return bet;
    }
}
