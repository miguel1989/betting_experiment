package com.neotech.service;

import com.neotech.domain.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface UserService {

    List<User> findAll();
    User getCurrentUser();
    User register(String name, String username, String email, String rawPass);
    User register(String name, String username, String email, String rawPass, List<GrantedAuthority> roles);

    void deleteAll();
}
