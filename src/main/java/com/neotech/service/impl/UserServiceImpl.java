package com.neotech.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.neotech.dao.UserDao;
import com.neotech.domain.Roles;
import com.neotech.domain.User;
import com.neotech.service.UserService;
import com.neotech.util.CoreUtils;
import com.neotech.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserValidator userValidator;

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User getCurrentUser() {
        String username = User.getCurrentUserUsername();
        return !Strings.isNullOrEmpty(username) ? userDao.findByUsername(username) : null;
    }

    /**
     * Register mortal user with "USER" role
     * @param name
     * @param username
     * @param email
     * @param rawPass
     * @return
     */
    @Override
    public User register(String name, String username, String email, String rawPass){
        List<GrantedAuthority> roles = Lists.newArrayList();
        roles.add(new SimpleGrantedAuthority(Roles.USER.toString()));
        return this.register(name, username, email, rawPass, roles);
    }

    /**
     * should be used internaly to create admin
     * @param name
     * @param username
     * @param email
     * @param rawPass
     * @param roles
     * @return
     */
    @Override
    public User register(String name, String username, String email, String rawPass, List<GrantedAuthority> roles) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(rawPass);
        newUser.setRoles(roles);

        return this.create(newUser);
    }

    private User create(User user) {
        userValidator.validate(user);
        user.setCreatedTs(CoreUtils.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    @Override
    public void deleteAll(){
        userDao.deleteAll();
    }
}
