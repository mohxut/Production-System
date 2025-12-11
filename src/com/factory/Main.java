package com.factory;

import com.factory.persistence.UserRepository;
import com.factory.service.AuthService;
import com.factory.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        UserRepository repo = new UserRepository();
        AuthService auth = new AuthService(repo);

        SwingUtilities.invokeLater(() -> {
            new LoginFrame(auth).setVisible(true);
        });
    }
}
