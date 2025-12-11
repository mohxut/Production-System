package com.factory.persistence;

import com.factory.model.Role;
import com.factory.model.User;
import com.factory.util.HashUtils;

import java.util.HashMap;

public class UserRepository {

    private HashMap<String, User> users = new HashMap<>();

    public UserRepository() {
        users.put("admin", new User("admin", HashUtils.sha256("admin123"), Role.ADMIN));
        users.put("super", new User("super", HashUtils.sha256("super123"), Role.SUPERVISOR));
    }

    public User findByUsername(String username) {
        return users.get(username);
    }
}
