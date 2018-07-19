package com.neotech.controller;

import com.neotech.domain.Setting;
import com.neotech.domain.User;
import com.neotech.domain.UserRegisterDTO;
import com.neotech.service.InitService;
import com.neotech.service.SettingService;
import com.neotech.service.UserService;
import com.neotech.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private InitService initService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "/static/app/index";
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ResponseBody
    public User signUp(@RequestBody UserRegisterDTO userRegisterDTO) {
        //create mortal user with role "USER"
        User createdUser = userService.register(userRegisterDTO.getName(), userRegisterDTO.getUsername(), userRegisterDTO.getEmail(), userRegisterDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(createdUser.getUsername(), null, createdUser.getAuthorities()));
        return createdUser;
    }

    @RequestMapping(value = Constants.REST_API_PREFIX + "/settings", method = RequestMethod.GET)
    @ResponseBody
    public Setting getSettings() {
        return settingService.findDefault();
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/init-data")
    @ResponseBody
    public String initData() {
        initService.initData();
        return "Data init success";
    }
}
