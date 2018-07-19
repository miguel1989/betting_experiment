package com.neotech.controller;

import com.neotech.domain.Bet;
import com.neotech.domain.BetSlip;
import com.neotech.domain.PlaceBetDTO;
import com.neotech.service.BetService;
import com.neotech.service.BetSlipService;
import com.neotech.util.Constants;
import com.neotech.util.CoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(Constants.REST_API_PREFIX + "/bets")
public class BetController {

    @Autowired
    private BetService betService;
    @Autowired
    private BetSlipService betSlipService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Bet> findAll(){
        return betService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Bet findOne(@PathVariable("id") String id) {
        return betService.findOne(id);
    }
    @RequestMapping(value = "/{id}/place-bet", method = RequestMethod.POST)
    public BetSlip placeBet(HttpServletRequest request,
                            @PathVariable("id") String id,
                            @RequestBody PlaceBetDTO placeBetDTO) {
        placeBetDTO.setIp(CoreUtils.getIp(request));
        return betSlipService.create(placeBetDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public Bet create(@RequestBody Bet bet) {
        return betService.create(bet);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Bet update(@PathVariable("id") String id, @RequestBody Bet bet) {
        return betService.update(bet);
    }
}
