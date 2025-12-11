package com.factory.ui;

import com.factory.model.User;
import com.factory.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class SupervisorFrame extends JFrame {

    private AuthService authService;

    public SupervisorFrame(User user, AuthService authService) {
        this.authService = authService;

        setTitle("Supervisor Dashboard");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI(user);
    }

    private void initUI(User user) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(33, 47, 60));
        panel.setLayout(null);

        JLabel title = new JLabel("Welcome Supervisor: " + user.getUsername());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(150, 20, 500, 40);
        panel.add(title);

        JButton inventoryBtn = createButton("Manage Inventory", 80);
        JButton tasksBtn = createButton("Production Tasks", 140);
        JButton productsBtn = createButton("Products Management", 200);
        JButton logoutBtn = createButton("Logout", 260);

        logoutBtn.addActionListener(e -> {
            new LoginFrame(authService).setVisible(true);
            dispose();
        });

        panel.add(inventoryBtn);
        panel.add(tasksBtn);
        panel.add(productsBtn);
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
