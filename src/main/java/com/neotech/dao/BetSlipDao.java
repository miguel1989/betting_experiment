package com.neotech.dao;

import com.neotech.domain.BetSlip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetSlipDao extends MongoRepository<BetSlip,String>, BetSlipDaoCustom {
    BetSlip findByTimestamp(long timestamp);
}
