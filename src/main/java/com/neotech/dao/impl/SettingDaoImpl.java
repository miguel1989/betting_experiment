package com.neotech.dao.impl;

import com.neotech.dao.SettingDaoCustom;
import com.neotech.domain.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Map;

@NoRepositoryBean
public class SettingDaoImpl implements SettingDaoCustom {
    @Autowired
    MongoOperations mongoOperations;

    @Override
    public Setting updateValues(String name, Map<String, Object> values) {
        Query query = new Query(Criteria.where("name").is(name));
        Update update = new Update().set("values", values);

        return mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Setting.class);
    }
}
