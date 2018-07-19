package com.neotech.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.neotech.controller.exception.ResourceNotFoundException;
import com.neotech.dao.SettingDao;
import com.neotech.domain.Setting;
import com.neotech.service.SettingService;
import com.neotech.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class SettingServiceImpl implements SettingService {
    @Autowired
    SettingDao settingDao;

    @Override
    public Setting findDefault() {
        return this.findByName(Constants.DEFAULT_SETTING_NAME);
    }

    @Override
    public Setting createDefault() {
        Setting setting = new Setting();
        setting.updateTimeStamp();
        setting.setName(Constants.DEFAULT_SETTING_NAME);
        Map<String, Object> valueMap = Maps.newHashMap();
        valueMap.put(Constants.SETTING_MIN_BET_AMOUNT, new BigDecimal(1).toString());      //bigdecimal is stored as string in mongo
        valueMap.put(Constants.SETTING_MAX_BET_AMOUNT, new BigDecimal(100).toString());
        setting.setValues(valueMap);
        return settingDao.save(setting);
    }

    @Override
    public Setting updateValues(String name, Map<String, Object> values) {
        if (Strings.isNullOrEmpty(name)) {
            throw new ResourceNotFoundException();
        }
        Setting result = settingDao.updateValues(name, values);
        if (result == null) {
            throw new ResourceNotFoundException();
        }
        return result;
    }

    @Override
    public Setting deleteDefault() {
        Setting setting = this.findByName(Constants.DEFAULT_SETTING_NAME);
        settingDao.delete(setting.getId());
        return setting;
    }
    @Override
    public void deleteAll() {
        settingDao.deleteAll();
    }

    private Setting findByName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new ResourceNotFoundException();
        }
        Setting setting = settingDao.findByName(name);
        if (setting == null) {
            throw new ResourceNotFoundException();
        }
        return setting;
    }
}
