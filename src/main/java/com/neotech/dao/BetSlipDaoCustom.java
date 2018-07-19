package com.neotech.dao;

import com.neotech.domain.BetSlip;
import com.neotech.domain.OddType;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface BetSlipDaoCustom {

    List<BetSlip> genericSearch(String betId, OddType type, String ip);

    Map<String, Long> aggregateBetCountByIp();
}
