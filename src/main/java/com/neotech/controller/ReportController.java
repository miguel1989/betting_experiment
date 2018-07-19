package com.neotech.controller;

import com.neotech.domain.BetSlip;
import com.neotech.domain.OddType;
import com.neotech.service.BetSlipService;
import com.neotech.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//Secured for ADMIN by SecurityConfig
@RestController
@RequestMapping(Constants.REST_API_PREFIX + "/reports")
public class ReportController {
    @Autowired
    private BetSlipService betSlipService;

    @RequestMapping(method = RequestMethod.GET)
    public List<BetSlip> genericSearch(@RequestParam(name = "betId", required = false) String betId,
                                       @RequestParam(name = "type", required = false) OddType type,
                                       @RequestParam(name = "ip", required = false) String ip){
        return betSlipService.genericSearch(betId, type, ip);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/aggregate-count-by-ip")
    public Map<String, Long> aggregateBetCountByIp(){
        return betSlipService.aggregateBetCountByIp();
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/{ts}")
    public BetSlip deleteByTs(@PathVariable("ts") Long timestamp) {
        return betSlipService.deleteByTimestamp(timestamp);
    }
}
