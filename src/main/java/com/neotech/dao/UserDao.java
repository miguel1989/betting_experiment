package com.neotech.dao;

import com.neotech.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends MongoRepository<User, String> {

    User findByUsername(String userName);
}
