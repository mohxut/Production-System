package com.factory.ui;

import com.factory.model.User;
import com.factory.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {

    private AuthService authService;

    public AdminFrame(User user, AuthService authService) {
        this.authService = authService;

        setTitle("Admin Dashboard");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI(user);
    }

    private void initUI(User user) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(23, 32, 42));
        panel.setLayout(null);

        JLabel title = new JLabel("Welcome Admin: " + user.getUsername());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(150, 20, 400, 40);
        panel.add(title);

        JButton manageLinesBtn = createButton("Manage Production Lines", 80);
        JButton manageUsersBtn = createButton("Manage Users", 140);
        JButton reportsBtn = createButton("View Reports", 200);
        JButton logoutBtn = createButton("Logout", 260);

        logoutBtn.addActionListener(e -> {
            new LoginFrame(authService).setVisible(true);
            dispose();
        });

        panel.add(manageLinesBtn);
        panel.add(manageUsersBtn);
        panel.add(reportsBtn);
        panel.add(logoutBtn);

        add(panel);
    }

    private JButton createButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(200, y, 300, 40);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(52, 152, 219));
        btn.setBorder(BorderFactory.createEmptyBorder());
        return btn;
    }
}
