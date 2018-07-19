package com.neotech.service;

import com.neotech.Application;
import com.neotech.config.EmbedMongoConfig;
import com.neotech.config.MainConfig;
import com.neotech.config.SecurityConfig;
import com.neotech.config.WebConfig;
import com.neotech.controller.exception.ResourceNotFoundException;
import com.neotech.controller.exception.ValidationException;
import com.neotech.domain.Bet;
import com.neotech.domain.Odd;
import com.neotech.util.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
public class BetServiceTest {
    @Autowired
    private BetService betService;

    @Before
    public void setUp() {
        betService.deleteAll();
    }

    @Test
    public void createSuccess() {
        Bet bet = new Bet();
        bet.setId("test");
        bet.setName("Russia vs USA");
        bet.setOdd(new Odd("1x2", 1.3, 2.4, 3.5));

        Bet savedBet = betService.create(bet);
        assertThat(savedBet, is(notNullValue()));
        assertThat(savedBet.getId(), is(notNullValue()));
        assertThat(savedBet.getId(), is(not(equalTo("test"))));
        assertThat(savedBet.getTimestamp(), is(not(0l)));
        assertThat(savedBet.getOdd(), is(notNullValue()));
        assertThat(savedBet.getOdd().getName(), equalTo("1x2"));
        assertThat(savedBet.getOdd().getWin(), equalTo(1.3));
        assertThat(savedBet.getOdd().getDraw(), equalTo(2.4));
        assertThat(savedBet.getOdd().getLose(), equalTo(3.5));

        Bet foundBet = betService.findOne(savedBet.getId());
        assertThat(foundBet, is(not(nullValue())));

        List<Bet> allBets = betService.findAll();
        assertThat(allBets, hasSize(1));
    }

    @Test
    public void createFail_emptyBet() {
        Bet bet = new Bet();
        try{
            betService.create(bet);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getFieldErrors().size(), equalTo(2));
            assertThat(ex.getResponse().getFieldErrors(), hasKey("name"));  //#todo maybe also check errorCodes
            assertThat(ex.getResponse().getFieldErrors(), hasKey("odd"));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void createFail_wrongOddValues() {
        String longName = RandomStringUtils.randomAlphanumeric(Constants.MAX_NAME_LENGTH*2);
        Bet bet = new Bet();
        bet.setName("Latvia vs Gana");
        bet.setOdd(new Odd(longName, 0, -2.5, -3.5));

        try{
            betService.create(bet);
        } catch (ValidationException ex) {
            assertThat(ex.getResponse().getFieldErrors().size(), equalTo(4));
            assertThat(ex.getResponse().getFieldErrors(), hasKey("name"));   //#todo maybe also check errorCodes
            assertThat(ex.getResponse().getFieldErrors(), hasKey("win"));
            assertThat(ex.getResponse().getFieldErrors(), hasKey("draw"));
            assertThat(ex.getResponse().getFieldErrors(), hasKey("lose"));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void updateSuccess(){
        Bet bet = new Bet();
        bet.setName("Russia vs USA");
        bet.setOdd(new Odd("1x2", 1.3, 2.4, 3.5));

        Bet savedBet = betService.create(bet);
        assertThat(savedBet, is(notNullValue()));

        savedBet.setName("Russia vs USA (upd)");
        savedBet.setOdd(new Odd("1x2", 2.3, 3.4, 4.5));
        Bet updatedBet = betService.update(savedBet);
        assertThat(updatedBet, is(notNullValue()));
        assertThat(updatedBet.getId(), is(equalTo(savedBet.getId())));
        assertThat(updatedBet.getTimestamp(), is(not(0l)));
        assertThat(updatedBet.getTimestamp(), is(not(equalTo(savedBet.getTimestamp()))));
        assertThat(updatedBet.getOdd().getName(), equalTo("1x2"));
        assertThat(updatedBet.getOdd().getWin(), equalTo(2.3));
        assertThat(updatedBet.getOdd().getDraw(), equalTo(3.4));
        assertThat(updatedBet.getOdd().getLose(), equalTo(4.5));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateFail_wrongBetId() {
        Bet bet = new Bet();
        bet.setId("WRONG");
        bet.setName("Russia vs USA");
        bet.setOdd(new Odd("1x2", 1.3, 2.4, 3.5));
        betService.update(bet);
    }

    @Test
    public void updateFail_ConcurrentModification() {
        Bet bet = new Bet();
        bet.setName("Russia vs USA");
        bet.setOdd(new Odd("1x2", 1.3, 2.4, 3.5));
        Bet createdBet = betService.create(bet);

        createdBet.setName("upd1");
        betService.update(createdBet);

        try{
            betService.update(createdBet);
        }catch (ValidationException ex) {
            assertThat(ex.getResponse().getErrors(), hasSize(1));
            assertThat(ex.getResponse().getErrors().get(0).getErrorCode(), equalTo(Constants.ERR_CONCURRENT_MODIFICATION));
            return;
        }
        assertThat(true, is(false));  //to fail test
    }

    @Test
    public void deleteByTimestampSuccess() {
        Bet bet = new Bet();
        bet.setName("Russia vs USA");
        bet.setOdd(new Odd("1x2", 1.3, 2.4, 3.5));

        Bet savedBet = betService.create(bet);
        assertThat(savedBet, is(notNullValue()));

        List<Bet> allBets = betService.findAll();
        assertThat(allBets, hasSize(1));

        Bet deleted = betService.deleteByTimestamp(savedBet.getTimestamp());
        assertThat(deleted, is(notNullValue()));

        allBets = betService.findAll();
        assertThat(allBets, hasSize(0));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteByTimestampFail() {
        betService.deleteByTimestamp(123l);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByIdFail_idIsNull() {
        betService.findOne(null);
    }
    @Test(expected = ResourceNotFoundException.class)
    public void findByIdFail_WrongId() {
        betService.findOne("superman");
    }
}
