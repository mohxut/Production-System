    package com.factory.model;

    public class User {
        private String username;
        private Role role;
        private String passwordHash;

        public User(String usernam ,String passwordHash, Role role) {
            this.username = username;
            this.passwordHash = passwordHash;
            this.role = role;
        }

        public String getUsername() { return username; }
        public String getPasswordHash() { return passwordHash; }
        public Role getRole() { return role; }
    }
