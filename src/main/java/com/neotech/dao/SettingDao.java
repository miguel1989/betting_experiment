package com.neotech.dao;

import com.neotech.domain.Setting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingDao extends MongoRepository<Setting, String>, SettingDaoCustom {
    Setting findByName(String name);
}
