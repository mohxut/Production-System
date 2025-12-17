    package com.factory.model;

    public class User {
        private String username;
        private String passwordHash;
        private Role role;

        public User(String username, String passwordHash, Role role) {
            this.username = username;
            this.passwordHash = passwordHash;
            this.role = role;
        }

        public String getUsername() { return username; }
        public String getPasswordHash() { return passwordHash; }
        public Role getRole() { return role; }
    }
