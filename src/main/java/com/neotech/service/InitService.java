package com.neotech.service;

import com.google.common.collect.Lists;
import com.neotech.dao.BetDao;
import com.neotech.dao.BetSlipDao;
import com.neotech.domain.Bet;
import com.neotech.domain.Odd;
import com.neotech.domain.Roles;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class InitService implements InitializingBean {
    @Autowired
    private UserService userService;
    @Autowired
    private BetDao betDao;
    @Autowired
    private BetSlipDao betSlipDao;
    @Autowired
    private BetService betService;
    @Autowired
    private SettingService settingService;

    public void initData(){
        this.initUsers();
        this.initBets();
    }

    private void initSettings(){
        settingService.deleteAll();
        settingService.createDefault();
    }

    private void initUsers() {
        userService.deleteAll();

        List<GrantedAuthority> adminRoles = Lists.newArrayList();
        adminRoles.add(new SimpleGrantedAuthority(Roles.ADMIN.toString()));

        userService.register("admin", "admin", "admin@neotech.lv", "admin", adminRoles);
        userService.register("user", "user", "user@neotech.lv", "user");
    }

    private void initBets() {
        betDao.deleteAll();
        betSlipDao.deleteAll();

        Bet bet1 = new Bet();
        bet1.setName("Russia - France");
        bet1.setOdd(new Odd("1x2", 1.5, 3.5, 2.4));

        Bet bet2 = new Bet();
        bet2.setName("Germany - Italy");
        bet2.setOdd(new Odd("1x2", 2.3, 4.0, 3.4));

        Bet bet3 = new Bet();
        bet3.setName("Latvia - Senegal");
        bet3.setOdd(new Odd("1x2", 9.3, 8.1, 15.6));

        Bet bet4 = new Bet();
        bet4.setName("Batman - Superman");
        bet4.setOdd(new Odd("1x2", 1.4, 1.5, 1.6));

        betService.create(bet1);
        betService.create(bet2);
        betService.create(bet3);
        betService.create(bet4);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initSettings();
    }
}
