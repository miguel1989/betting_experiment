package com.neotech.service;

import com.neotech.domain.Bet;
import java.util.List;

public interface BetService {

    List<Bet> findAll();

    Bet findOne(String id);

    Bet create(Bet bet);
    Bet update(Bet bet);

    Bet deleteByTimestamp(long timestamp);
    void deleteAll();
}
