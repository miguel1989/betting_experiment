package com.neotech.service;

import com.neotech.domain.BetSlip;
import com.neotech.domain.OddType;
import com.neotech.domain.PlaceBetDTO;
import java.util.List;
import java.util.Map;

public interface BetSlipService {
    List<BetSlip> findAll();

    BetSlip create(PlaceBetDTO placeBetDTO);
    BetSlip deleteByTimestamp(long timestamp);

    List<BetSlip> genericSearch(String betId, OddType type, String ip);
    Map<String, Long> aggregateBetCountByIp();
}
