package com.factory;

import com.factory.persistence.ProductLineRepository;
import com.factory.persistence.TaskRepository;
import com.factory.persistence.UserRepository;
import com.factory.service.AuthService;
import com.factory.ui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        UserRepository userRepository = new UserRepository();
        AuthService authService = new AuthService(userRepository);

        ProductLineRepository plRepo = new ProductLineRepository();
        TaskRepository taskRepo = new TaskRepository();

        AppContext ctx = new AppContext(plRepo, taskRepo);

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(authService, ctx);
            login.setVisible(true);
        });
    }
}
