package com.neotech.dao;

import com.neotech.domain.Bet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetDao extends MongoRepository<Bet,String>, BetDaoCustom {
    Bet findByTimestamp(long timestamp);
}
