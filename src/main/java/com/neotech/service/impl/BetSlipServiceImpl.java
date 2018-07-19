package com.neotech.service.impl;

import com.neotech.controller.exception.ResourceNotFoundException;
import com.neotech.dao.BetSlipDao;
import com.neotech.domain.Bet;
import com.neotech.domain.BetSlip;
import com.neotech.domain.OddType;
import com.neotech.domain.PlaceBetDTO;
import com.neotech.service.BetSlipService;
import com.neotech.validation.PlaceBetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BetSlipServiceImpl implements BetSlipService {

    @Autowired
    private PlaceBetValidator placeBetValidator;
    @Autowired
    private BetSlipDao betSlipDao;

    @Override
    public List<BetSlip> findAll() {
        return betSlipDao.findAll();
    }

    @Override
    public BetSlip create(PlaceBetDTO placeBetDTO) {
        Bet oldBet = placeBetValidator.validate(placeBetDTO);

        BetSlip betSlip = new BetSlip();
        betSlip.setBetId(oldBet.getId());
        betSlip.setName(oldBet.getName());
        betSlip.setType(placeBetDTO.getType());
        betSlip.setOdd(placeBetDTO.getOdd());
        betSlip.setAmount(placeBetDTO.getAmount());
        betSlip.setIp(placeBetDTO.getIp());
        betSlip.updateTimeStamp();

        return betSlipDao.save(betSlip);
    }

    @Override
    public BetSlip deleteByTimestamp(long timestamp) {
        BetSlip betSlip = betSlipDao.findByTimestamp(timestamp);
        if (betSlip == null) {
            throw new ResourceNotFoundException();
        }
        betSlipDao.delete(betSlip.getId());

        return betSlip;
    }

    @Override
    public List<BetSlip> genericSearch(String betId, OddType type, String ip){
        return betSlipDao.genericSearch(betId, type, ip);
    }

    @Override
    public Map<String, Long> aggregateBetCountByIp() {
        return betSlipDao.aggregateBetCountByIp();
    }
}
