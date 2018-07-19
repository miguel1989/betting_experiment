package com.neotech.worker;

import com.neotech.controller.exception.ValidationException;
import com.neotech.domain.Bet;
import com.neotech.service.BetService;
import com.neotech.util.Constants;
import com.neotech.util.CoreUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class Worker {
    private static final Logger log = LoggerFactory.getLogger(Worker.class);

    private static final int UPDATE_BETS_RATE = 10000;  //10 seconds
    private static final int UPDATE_BETS_RATE2 = 2000;  //2 seconds
    private static final int UPDATE_BETS_RATE3 = 1000;  //1 seconds
    @Autowired
    private BetService betService;

    private Random random = new Random();

    @Scheduled(fixedRate = UPDATE_BETS_RATE)
    public void updateBets1() {
        log.debug("Updating bets1");
        this.updateBets();
    }
    /*
    @Scheduled(fixedRate = UPDATE_BETS_RATE2)
    public void updateBets2() {
        log.debug("Updating bets2");
        this.updateBets();
    }
    @Scheduled(fixedRate = UPDATE_BETS_RATE3)
    public void updateBets3() {
        log.debug("Updating bets3");
        this.updateBets();
    }   */

    private void updateBets() {
        List<Bet> bets = betService.findAll();
        bets.stream().forEach(this::updateSingleBet);
    }

    private void updateSingleBet(Bet bet) {
        //30% chance to update single bet
        double r = random.nextDouble();
        if (r > 0.3) {
            return;
        }

        double winOdd = (random.nextDouble() * (Constants.MAX_BET_ODD - Constants.MIN_BET_ODD)) + Constants.MIN_BET_ODD;
        double drawOdd = (random.nextDouble() * (Constants.MAX_BET_ODD - Constants.MIN_BET_ODD)) + Constants.MIN_BET_ODD;
        double loseOdd = (random.nextDouble() * (Constants.MAX_BET_ODD - Constants.MIN_BET_ODD)) + Constants.MIN_BET_ODD;
        bet.getOdd().setWin(CoreUtils.roundDouble(winOdd, Constants.DECIMAL_SCALE));
        bet.getOdd().setDraw(CoreUtils.roundDouble(drawOdd, Constants.DECIMAL_SCALE));
        bet.getOdd().setLose(CoreUtils.roundDouble(loseOdd, Constants.DECIMAL_SCALE));
        try{
            Bet savedBet = betService.update(bet);
            log.debug("Updated bet = " + savedBet);
        } catch (ValidationException ex) {
            log.debug("Failed to update bet, it was already update by some one else " + bet);
        }
    }
}
