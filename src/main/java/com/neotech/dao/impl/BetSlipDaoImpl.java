package com.neotech.dao.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.neotech.dao.BetSlipDaoCustom;
import com.neotech.domain.BetSlip;
import com.neotech.domain.OddType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public class BetSlipDaoImpl implements BetSlipDaoCustom {
    @Autowired
    MongoOperations mongoOperations;

    @Override
    public List<BetSlip> genericSearch(String betId, OddType type, String ip) {
        Query query = new Query();
        if (!Strings.isNullOrEmpty(betId)) {
            query.addCriteria(Criteria.where("betId").is(betId));
        }
        if (!Strings.isNullOrEmpty(ip)) {
            query.addCriteria(Criteria.where("ip").is(ip));
        }
        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        return mongoOperations.find(query, BetSlip.class);
    }

    //db.betSlips.aggregate([{$group:{_id:"$ip", "count":{$sum:1}}}])
    @Override
    public Map<String, Long> aggregateBetCountByIp() {
        DBCollection dbCollection = mongoOperations.getCollection(mongoOperations.getCollectionName(BetSlip.class));

        Map<String, Long> resultMap = Maps.newHashMap();

        DBObject groupCriteria = new BasicDBObject("_id", "$ip");
        groupCriteria.put("count", new BasicDBObject("$sum",1));
        DBObject group = new BasicDBObject("$group", groupCriteria);

        List<DBObject> pipeline = Lists.newArrayList(group);

        Iterable<DBObject> resultIterable;
        try{
            resultIterable = dbCollection.aggregate(pipeline).results();
        }catch (Exception ex){                         //IllegalArgumentException: result undefined
            return resultMap;
        }

        resultIterable.forEach(dbObj -> {
            long count = 0l;
            String ip = dbObj.get("_id").toString();
            if (dbObj.get("count") instanceof Integer){
                count = ((Integer)dbObj.get("count")).longValue();
            }
            else if (dbObj.get("count") instanceof Long){
                count = (Long)dbObj.get("count");
            }
            resultMap.put(ip, count);
        });

        return resultMap;
    }
}
