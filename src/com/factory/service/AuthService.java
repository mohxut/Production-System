package com.factory.service;

import com.factory.model.User;
import com.factory.persistence.UserRepository;
import com.factory.util.HashUtils;

public class AuthService {

    private UserRepository userRepo;

    public AuthService(UserRepository repo) {
        this.userRepo = repo;
    }

    public User login(String username, String password) {
        User user = userRepo.findByUsername(username);

        if (user == null)
            return null;

        String hashed = HashUtils.sha256(password);

        if (hashed.equals(user.getPasswordHash()))
            return user;

        return null;
    }
}
