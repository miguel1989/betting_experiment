package com.neotech.service;

import com.google.common.collect.Maps;
import com.neotech.Application;
import com.neotech.config.EmbedMongoConfig;
import com.neotech.config.MainConfig;
import com.neotech.config.SecurityConfig;
import com.neotech.config.WebConfig;
import com.neotech.controller.exception.ResourceNotFoundException;
import com.neotech.domain.Setting;
import com.neotech.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
        MainConfig.class,
        Application.class,
        SecurityConfig.class,
        EmbedMongoConfig.class,
        WebConfig.class})
public class SettingsServiceTest {

    @Autowired
    SettingService settingService;

    @Before
    public void setUp() {
        settingService.deleteAll();
    }


    @Test(expected = ResourceNotFoundException.class)
    public void findDefault_fail() {
        settingService.findDefault();
    }

    @Test
    public void createDefault() {
        Setting setting = settingService.createDefault();
        assertThat(setting, is(notNullValue()));
        assertThat(setting.getId(), is(notNullValue()));
        assertThat(setting.getTimestamp(), is(greaterThan(0l)));
        assertThat(setting.getValues(), is(notNullValue()));
        assertThat(setting.getValues().size(), equalTo(2));
        assertThat(setting.getValues(), hasKey(Constants.SETTING_MIN_BET_AMOUNT));
        assertThat(setting.getValues(), hasKey(Constants.SETTING_MAX_BET_AMOUNT));
        Object minValObj = setting.getValues().get(Constants.SETTING_MIN_BET_AMOUNT);
        Object maxValObj = setting.getValues().get(Constants.SETTING_MAX_BET_AMOUNT);
        assertThat((String)minValObj, equalTo(new BigDecimal(1).toString()));
        assertThat((String)maxValObj, equalTo(new BigDecimal(100).toString()));

        Setting defaultSetting = settingService.findDefault();
        assertThat(defaultSetting, is(notNullValue()));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateValues_fail() {
        settingService.updateValues("wrong", Maps.newHashMap());
    }

    @Test
    public void updateValues_success() {
        settingService.createDefault();

        Map<String, Object> valueMap = Maps.newHashMap();
        valueMap.put(Constants.SETTING_MIN_BET_AMOUNT, new BigDecimal(2).toString());
        valueMap.put(Constants.SETTING_MAX_BET_AMOUNT, new BigDecimal(200).toString());
        Setting setting = settingService.updateValues(Constants.DEFAULT_SETTING_NAME, valueMap);
        assertThat(setting, is(notNullValue()));
        assertThat(setting.getValues().size(), equalTo(2));
        assertThat(setting.getValues(), hasKey(Constants.SETTING_MIN_BET_AMOUNT));
        assertThat(setting.getValues(), hasKey(Constants.SETTING_MAX_BET_AMOUNT));
        Object minValObj = setting.getValues().get(Constants.SETTING_MIN_BET_AMOUNT);
        Object maxValObj = setting.getValues().get(Constants.SETTING_MAX_BET_AMOUNT);
        assertThat((String)minValObj, equalTo(new BigDecimal(2).toString()));
        assertThat((String)maxValObj, equalTo(new BigDecimal(200).toString()));
    }
}
