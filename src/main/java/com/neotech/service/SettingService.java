package com.neotech.service;

import com.neotech.domain.Setting;

import java.util.Map;

public interface SettingService {

    Setting findDefault();
    Setting createDefault();
    Setting updateValues(String name, Map<String, Object> values);
    Setting deleteDefault();
    void deleteAll();
}
