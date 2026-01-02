package com.factory.ui;

import com.factory.model.User;
import com.factory.service.AuthService;
import com.factory.AppContext;
import com.factory.persistence.MaterialRepository;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private  MaterialRepository MatRepo;
    private AuthService authService;
    private AppContext ctx; // NEW

    public LoginFrame(AuthService authService, MaterialRepository MatRepo   , AppContext ctx) {
        this.authService = authService;
        this.ctx = ctx;
        this.MatRepo = MatRepo;

        setTitle("PRODUCTION SYSTEM - Login");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    public LoginFrame(AuthService authService , AppContext ctx) {
        this.authService = authService;
        this.ctx = ctx;

        setTitle("PRODUCTION SYSTEM - Login");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(40, 55, 71));
        mainPanel.setLayout(null);

        JLabel title = new JLabel("PRODUCTION-SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(90, 20, 300, 40);
        mainPanel.add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userLabel.setBounds(50, 90, 100, 25);
        mainPanel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 90, 200, 28);
        userField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passLabel.setBounds(50, 140, 100, 25);
        mainPanel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 140, 200, 28);
        passField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 190, 200, 35);
        loginBtn.setFocusPainted(false);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            User user = authService.login(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
                return;
            }

            switch (user.getRole()) {
                case ADMIN:
                    new AdminFrame(user, MatRepo  ,authService).setVisible(true);
                    break;

                case SUPERVISOR:
                    new SuperManagerFrame(user, authService, ctx).setVisible(true);
                    break;
            }

            dispose();
        });

        add(mainPanel);
    }
}
