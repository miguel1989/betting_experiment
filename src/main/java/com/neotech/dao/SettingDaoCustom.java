package com.neotech.dao;

import com.neotech.domain.Setting;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Map;

@NoRepositoryBean
public interface SettingDaoCustom {
    Setting updateValues(String name, Map<String, Object> values);
}
