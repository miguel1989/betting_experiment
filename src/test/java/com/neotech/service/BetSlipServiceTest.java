package com.neotech.service;

import com.neotech.Application;
import com.neotech.config.EmbedMongoConfig;
import com.neotech.config.MainConfig;
import com.neotech.config.SecurityConfig;
import com.neotech.config.WebConfig;
import com.neotech.controller.exception.ResourceNotFoundException;
import com.neotech.controller.exception.ValidationException;
import com.neotech.dao.BetSlipDao;
import com.neotech.domain.*;
import com.neotech.util.Constants;
import com.neotech.util.ValidationError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
        MainConfig.class,
        Application.class,
        SecurityConfig.class,
        EmbedMongoConfig.class,
        WebConfig.class})
public class BetSlipServiceTest {

    private static final String FAKE_IP = "1.2.3.4";
    private static final String FAKE_IP2 = "192.168.1.1";
    private static final String FAKE_IP3 = "127.0.0.1";

    @Autowired
    private BetSlipDao betSlipDao;
    @Autowired
    private BetSlipService betSlipService;
    @Autowired
    private BetService betService;
    @Autowired
    private SettingService settingService;

    @Before
    public void setUp() {
        betSlipDao.deleteAll();
        betService.deleteAll();
        settingService.deleteAll();
        settingService.createDefault();
    }


    @Test
    public void createSuccess() {
        Bet bet = createNormalBet();

        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId(bet.getId());
        placeBetDTO.setTimestamp(bet.getTimestamp());
        placeBetDTO.setIp(FAKE_IP);
        placeBetDTO.setAmount(new Amount(new BigDecimal(5.5)));
        placeBetDTO.setOdd(bet.getOdd().getDraw());
        placeBetDTO.setType(OddType.DRAW);

        BetSlip savedBetSlip = betSlipService.create(placeBetDTO);
        assertThat(savedBetSlip, is(notNullValue()));
        assertThat(savedBetSlip.getId(), is(notNullValue()));
        assertThat(savedBetSlip.getTimestamp(), is(not(0l)));
        assertThat(savedBetSlip.getBetId(), is(equalTo(bet.getId())));
        assertThat(savedBetSlip.getName(), is(equalTo(bet.getName())));
        assertThat(savedBetSlip.getType(), is(equalTo(placeBetDTO.getType())));
        assertThat(savedBetSlip.getOdd(), is(equalTo(placeBetDTO.getOdd())));
        assertThat(savedBetSlip.getAmount(), is(equalTo(placeBetDTO.getAmount())));
        assertThat(savedBetSlip.getIp(), is(equalTo(placeBetDTO.getIp())));
    }

    @Test
    public void createFail_emptyPlaceBet(){
        PlaceBetDTO placeBetDTO = new PlaceBetDTO();

        try{
            betSlipService.create(placeBetDTO);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getFieldErrors().size(), equalTo(3));
            assertThat(ex.getResponse().getFieldErrors(), hasKey("amount"));
            assertThat(ex.getResponse().getFieldErrors(), hasKey("betId"));
            assertThat(ex.getResponse().getFieldErrors(), hasKey("type"));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void createFail_wrongBetId(){
        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId("batman");
        placeBetDTO.setTimestamp(123l);
        placeBetDTO.setIp(FAKE_IP);
        placeBetDTO.setAmount(new Amount(new BigDecimal(5.5)));
        placeBetDTO.setOdd(3.5);
        placeBetDTO.setType(OddType.DRAW);

        try{
            betSlipService.create(placeBetDTO);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getErrors(), hasSize(1));
            assertThat(ex.getResponse().getErrors().get(0).getErrorCode(), equalTo(Constants.ERR_NOT_VALID_BET));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void createFail_timeStampChanged(){
        Bet bet = createNormalBet();

        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId(bet.getId());
        placeBetDTO.setTimestamp(bet.getTimestamp());
        placeBetDTO.setIp(FAKE_IP);
        placeBetDTO.setAmount(new Amount(new BigDecimal(5.5)));
        placeBetDTO.setOdd(bet.getOdd().getDraw());
        placeBetDTO.setType(OddType.DRAW);

        //someone changes odds
        bet.getOdd().setWin(50);
        bet.getOdd().setDraw(100);
        bet.getOdd().setLose(300);
        betService.update(bet);

        try{
            betSlipService.create(placeBetDTO);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getErrors(), hasSize(1));
            assertThat(ex.getResponse().getErrors().get(0).getErrorCode(), equalTo(Constants.ERR_BET_CHANGED));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void createFail_oddMismatch_WIN(){
        Bet bet = createNormalBet();

        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId(bet.getId());
        placeBetDTO.setTimestamp(bet.getTimestamp());
        placeBetDTO.setIp(FAKE_IP);
        placeBetDTO.setAmount(new Amount(new BigDecimal(1000)));
        placeBetDTO.setOdd(5000);
        placeBetDTO.setType(OddType.WIN);

        try{
            betSlipService.create(placeBetDTO);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getErrors(), hasSize(1));
            assertThat(ex.getResponse().getErrors().get(0).getErrorCode(), equalTo(Constants.ERR_ODD_MISMATCH));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void createFail_oddMismatch_DRAW(){
        Bet bet = createNormalBet();

        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId(bet.getId());
        placeBetDTO.setTimestamp(bet.getTimestamp());
        placeBetDTO.setIp(FAKE_IP);
        placeBetDTO.setAmount(new Amount(new BigDecimal(1000)));
        placeBetDTO.setOdd(5000);
        placeBetDTO.setType(OddType.DRAW);

        try{
            betSlipService.create(placeBetDTO);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getErrors(), hasSize(1));
            assertThat(ex.getResponse().getErrors().get(0).getErrorCode(), equalTo(Constants.ERR_ODD_MISMATCH));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void createFail_oddMismatch_LOSE(){
        Bet bet = createNormalBet();

        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId(bet.getId());
        placeBetDTO.setTimestamp(bet.getTimestamp());
        placeBetDTO.setIp(FAKE_IP);
        placeBetDTO.setAmount(new Amount(new BigDecimal(1000)));
        placeBetDTO.setOdd(5000);
        placeBetDTO.setType(OddType.LOSE);

        try{
            betSlipService.create(placeBetDTO);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getErrors(), hasSize(1));
            assertThat(ex.getResponse().getErrors().get(0).getErrorCode(), equalTo(Constants.ERR_ODD_MISMATCH));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void deleteByTimestampSuccess() {
        Bet bet = createNormalBet();

        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId(bet.getId());
        placeBetDTO.setTimestamp(bet.getTimestamp());
        placeBetDTO.setIp(FAKE_IP);
        placeBetDTO.setAmount(new Amount(new BigDecimal(5.5)));
        placeBetDTO.setOdd(bet.getOdd().getDraw());
        placeBetDTO.setType(OddType.DRAW);

        BetSlip savedBetSlip = betSlipService.create(placeBetDTO);

        List<BetSlip> allBetSlips = betSlipService.findAll();
        assertThat(allBetSlips, hasSize(1));

        betSlipService.deleteByTimestamp(savedBetSlip.getTimestamp());

        allBetSlips = betSlipService.findAll();
        assertThat(allBetSlips, hasSize(0));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteByTimestampFail() {
        betSlipService.deleteByTimestamp(321l);
    }

    @Test
    public void createFail_noAmountValue() {
        Bet bet = createNormalBet();

        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId(bet.getId());
        placeBetDTO.setTimestamp(bet.getTimestamp());
        placeBetDTO.setIp(FAKE_IP);
        placeBetDTO.setAmount(new Amount());
        placeBetDTO.setOdd(bet.getOdd().getDraw());
        placeBetDTO.setType(OddType.DRAW);


        try{
            betSlipService.create(placeBetDTO);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getFieldErrors().size(), equalTo(1));
            assertThat(ex.getResponse().getFieldErrors(), hasKey("value"));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void createFail_minAmountValue() {
        Bet bet = createNormalBet();

        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId(bet.getId());
        placeBetDTO.setTimestamp(bet.getTimestamp());
        placeBetDTO.setIp(FAKE_IP);
        placeBetDTO.setAmount(new Amount(new BigDecimal(0)));
        placeBetDTO.setOdd(bet.getOdd().getDraw());
        placeBetDTO.setType(OddType.DRAW);


        try{
            betSlipService.create(placeBetDTO);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getFieldErrors().size(), equalTo(1));
            assertThat(ex.getResponse().getFieldErrors(), hasKey("value"));
            List<ValidationError> errorList = ex.getResponse().getFieldErrors().get("value");
            assertThat(errorList, hasSize(1));
            assertThat(errorList.get(0).getErrorCode(), equalTo(Constants.ERR_BET_AMOUNT_MIN_VALUE));
            assertThat(errorList.get(0).getValues().length, equalTo(1));
            assertThat(errorList.get(0).getValues()[0], equalTo("1.00"));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }
    @Test
    public void createFail_maxAmountValue() {
        Bet bet = createNormalBet();

        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId(bet.getId());
        placeBetDTO.setTimestamp(bet.getTimestamp());
        placeBetDTO.setIp(FAKE_IP);
        placeBetDTO.setAmount(new Amount(new BigDecimal(100)));
        placeBetDTO.setOdd(bet.getOdd().getDraw());
        placeBetDTO.setType(OddType.DRAW);


        try{
            betSlipService.create(placeBetDTO);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getFieldErrors().size(), equalTo(1));
            assertThat(ex.getResponse().getFieldErrors(), hasKey("value"));
            List<ValidationError> errorList = ex.getResponse().getFieldErrors().get("value");
            assertThat(errorList, hasSize(1));
            assertThat(errorList.get(0).getErrorCode(), equalTo(Constants.ERR_BET_AMOUNT_MAX_VALUE));
            assertThat(errorList.get(0).getValues().length, equalTo(1));
            assertThat(errorList.get(0).getValues()[0], equalTo("41.67"));//Draw coef is 2.4. 100/2.4 = 41.67
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void genericSearch(){
        Bet bet1 = createNormalBet();
        Bet bet2 = createNormalBet();
        Bet bet3 = createNormalBet();

        placeBet(bet1, OddType.WIN, FAKE_IP);
        placeBet(bet1, OddType.DRAW, FAKE_IP2);
        placeBet(bet1, OddType.LOSE, FAKE_IP3);

        placeBet(bet2, OddType.WIN, FAKE_IP);
        placeBet(bet2, OddType.DRAW, FAKE_IP2);

        placeBet(bet3, OddType.DRAW, FAKE_IP);

        //empty search -> should find all bets
        List<BetSlip> betSlips = betSlipService.genericSearch(null, null, null);
        assertThat(betSlips, hasSize(6));

        betSlips = betSlipService.genericSearch(null, null, FAKE_IP);
        assertThat(betSlips, hasSize(3));

        //all draw for event2
        betSlips = betSlipService.genericSearch(bet2.getId(), OddType.DRAW, null);
        assertThat(betSlips, hasSize(1));

        betSlips =  betSlipService.genericSearch(null, null, "something wrong");
        assertThat(betSlips, hasSize(0));
    }

    private Bet createNormalBet() {
        Bet bet = new Bet();
        bet.setName("Russia vs USA");
        Odd odd = new Odd("1x2", 1.3, 2.4, 3.5);
        bet.setOdd(odd);

        Bet savedBet = betService.create(bet);
        assertThat(savedBet, is(notNullValue()));
        return savedBet;
    }

    private BetSlip placeBet(Bet bet, OddType oddType, String ip) {  //value does not matter
        PlaceBetDTO placeBetDTO = new PlaceBetDTO();
        placeBetDTO.setBetId(bet.getId());
        placeBetDTO.setTimestamp(bet.getTimestamp());
        placeBetDTO.setIp(ip);
        placeBetDTO.setAmount(new Amount(new BigDecimal(4.5)));
        placeBetDTO.setType(oddType);
        switch (oddType) {
            case WIN:
                placeBetDTO.setOdd(bet.getOdd().getWin());
                break;
            case DRAW:
                placeBetDTO.setOdd(bet.getOdd().getDraw());
                break;
            case LOSE:
                placeBetDTO.setOdd(bet.getOdd().getLose());
                break;
        }
        return betSlipService.create(placeBetDTO);
    }
}
