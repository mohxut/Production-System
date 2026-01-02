package com.factory.ui;

import com.factory.persistence.MaterialRepository ;
import com.factory.model.User;
import com.factory.model.Material;
import com.factory.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminFrame extends JFrame {

    private AuthService authService;

    private MaterialRepository MatRepo;

public AdminFrame(User user,  MaterialRepository repo ,AuthService authService) {
    this.authService = authService;
    this.MatRepo = repo;
        setTitle("Admin Dashboard");
        setSize(500, 450);
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


        JButton manageRepo = createButton("Repository Management", 140);
        JButton manageLinesBtn = createButton("Manage Production Lines", 200);
        JButton logoutBtn = createButton("Logout", 260);

        manageRepo.setBounds(150, 120, 200, 40);
        manageLinesBtn.setBounds(150, 190, 200, 40);
        logoutBtn.setBounds(200, 290, 90, 30);

        manageRepo.addActionListener(e -> openRepositoryManagementUI());


        logoutBtn.addActionListener(e -> {
            new LoginFrame(authService,MatRepo ,null).setVisible(true);
            dispose();
        });



        panel.add(manageLinesBtn);
        panel.add(manageRepo);
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


    private void openRepositoryManagementUI() {
        JFrame repoFrame = new JFrame("Repository Management");
        repoFrame.setSize(450, 400);
        repoFrame.setLocationRelativeTo(null);
        repoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        repoFrame.setLayout(null);

        // أزرار العمليات
        JButton addBtn = new JButton("Add New Item");
        JButton viewBtn = new JButton("View Item");
        JButton editBtn = new JButton("Edit Item");
        JButton deleteBtn = new JButton("Delete Item");
        JButton searchBtn = new JButton("Search");
        JButton saveBtn = new JButton("Save Inventory");
        JButton backBtn = new JButton("Back");


        // تحديد مواقع الأزرار
        addBtn.setBounds(50, 30, 125, 27);
        viewBtn.setBounds(50, 65, 125, 27);
        editBtn.setBounds(50, 100, 125, 27);
        deleteBtn.setBounds(50,135 , 125, 27);
        searchBtn.setBounds(50, 170, 125, 27);
        saveBtn.setBounds(260, 310, 130, 23);
        backBtn.setBounds(50, 310, 90, 23);

        // إضافة الأزرار إلى الواجهة
        repoFrame.add(addBtn);
        repoFrame.add(viewBtn);
        repoFrame.add(editBtn);
        repoFrame.add(deleteBtn);
        repoFrame.add(searchBtn);
        repoFrame.add(saveBtn);
        repoFrame.add(backBtn);


        addBtn.addActionListener(e -> handleAddItem());
        viewBtn.addActionListener(e -> handleViewItem());
        editBtn.addActionListener(e -> handleEditItem());
        deleteBtn.addActionListener(e -> handleDeleteItem());
        searchBtn.addActionListener(e -> handleSearchItem());
        saveBtn.addActionListener(e -> SaveToCSV());
        backBtn.addActionListener(e -> repoFrame.dispose());

        repoFrame.setVisible(true);
    }

    private void handleAddItem() {
        JDialog addDialog = new JDialog(this, "Add New Item", true);
        addDialog.setSize(400, 300);
        addDialog.setLayout(null);
        addDialog.setLocationRelativeTo(null);

        // الحقول
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        JTextField categoryField = new JTextField();

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();

        JLabel minQuantityLabel = new JLabel("Minimum Quantity:");
        JTextField minQuantityField = new JTextField();

        JButton submitBtn = new JButton("Add");

        // تحديد المواقع
        nameLabel.setBounds(40, 20, 100, 25);
        nameField.setBounds(140, 20, 200, 25);

        categoryLabel.setBounds(40, 60, 100, 25);
        categoryField.setBounds(140, 60, 200, 25);

        priceLabel.setBounds(40, 100, 100, 25);
        priceField.setBounds(140, 100, 200, 25);

        quantityLabel.setBounds(40, 140, 100, 25);
        quantityField.setBounds(140, 140, 200, 25);

        minQuantityLabel.setBounds(40, 180, 100, 25);
        minQuantityField.setBounds(140, 180, 200, 25);

        submitBtn.setBounds(140, 220, 100, 30);

        // إضافة العناصر
        addDialog.add(nameLabel);
        addDialog.add(nameField);
        addDialog.add(categoryLabel);
        addDialog.add(categoryField);
        addDialog.add(quantityLabel);
        addDialog.add(quantityField);
        addDialog.add(priceLabel);
        addDialog.add(priceField);
        addDialog.add(minQuantityLabel);
        addDialog.add(minQuantityField);

        addDialog.add(submitBtn);

        // زر الإضافة
        submitBtn.addActionListener(e -> {
            String name = nameField.getText();
            String category = categoryField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            int minQuantity = Integer.parseInt(minQuantityField.getText());

            addItemToRepository(name, category,price ,quantity, minQuantity);

            JOptionPane.showMessageDialog(addDialog, "Item added successfully!");
            addDialog.dispose();
        });

        addDialog.setVisible(true);
    }
    private void handleViewItem() {
        JDialog viewDialog = new JDialog(this, "View Inventory", true);
        viewDialog.setSize(700, 400);
        viewDialog.setLocationRelativeTo(null);
        viewDialog.setLayout(new BorderLayout());

        // أسماء الأعمدة
        String[] columnNames = {
                "ID", "Name", "Category", "Price", "Quantity", "Min Quantity"
        };

        // جلب البيانات من الريبو
        java.util.List<Material> materials = new ArrayList<>(MatRepo.findAll());

        // تحويل البيانات إلى مصفوفة للجدول
        Object[][] data = new Object[materials.size()][6];
        for (int i = 0; i < materials.size(); i++) {
            Material m = materials.get(i);
            data[i][0] = m.getId();
            data[i][1] = m.getName();
            data[i][2] = m.getCategory();
            data[i][3] = m.getPrice();
            data[i][4] = m.getQuantity();
            data[i][5] = m.getMinQuantity();
        }

        // إنشاء الجدول
        JTable table = new JTable(data, columnNames);
        table.setEnabled(false); // للعرض فقط

        JScrollPane scrollPane = new JScrollPane(table);
        viewDialog.add(scrollPane, BorderLayout.CENTER);

        viewDialog.setVisible(true);
    }

    private void handleEditItem() {
        String name = JOptionPane.showInputDialog(this, "Enter item name to edit:");
        java.util.List<Material> matches = MatRepo.findAllByName(name);
        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item not found.");
            return;
        }

        Material material = matches.get(0); // نفترض أول تطابق

        JTextField priceField = new JTextField(String.valueOf(material.getPrice()));
        JTextField quantityField = new JTextField(String.valueOf(material.getQuantity()));
        JTextField minQuantityField = new JTextField(String.valueOf(material.getMinQuantity()));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Price:")); panel.add(priceField);
        panel.add(new JLabel("Quantity:")); panel.add(quantityField);
        panel.add(new JLabel("Min Quantity:")); panel.add(minQuantityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            MatRepo.editMateral(material.getId(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(quantityField.getText()),
                    Integer.parseInt(minQuantityField.getText()));
            JOptionPane.showMessageDialog(this, "Item updated successfully!");
        }
    }

    private void handleSearchItem() {
        JDialog searchDialog = new JDialog(this, "Search Inventory", true);
        searchDialog.setSize(400, 300);
        searchDialog.setLayout(null);
        searchDialog.setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        JTextField categoryField = new JTextField();

        JLabel statusLabel = new JLabel("Status:");
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Any", "Available", "Out of Stock", "Below Minimum"});

        JButton searchBtn = new JButton("Search");

        nameLabel.setBounds(30, 20, 100, 25);
        nameField.setBounds(140, 20, 200, 25);

        categoryLabel.setBounds(30, 60, 100, 25);
        categoryField.setBounds(140, 60, 200, 25);

        statusLabel.setBounds(30, 100, 100, 25);
        statusBox.setBounds(140, 100, 200, 25);

        searchBtn.setBounds(140, 150, 100, 30);

        searchDialog.add(nameLabel);
        searchDialog.add(nameField);
        searchDialog.add(categoryLabel);
        searchDialog.add(categoryField);
        searchDialog.add(statusLabel);
        searchDialog.add(statusBox);
        searchDialog.add(searchBtn);

        searchBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String status = (String) statusBox.getSelectedItem();

            Set<Material> resultSet = new HashSet<>(MatRepo.findAll());

            if (!name.isEmpty()) {
                resultSet.retainAll(MatRepo.findAllByName(name));
            }

            if (!category.isEmpty()) {
                resultSet.retainAll(MatRepo.findAllByCategory(category));
            }

            switch (status) {
                case "Out of Stock":
                    resultSet.retainAll(MatRepo.findOutOfStock());
                    break;
                case "Below Minimum":
                    resultSet.retainAll(MatRepo.findAllBelowMinimum());
                    break;
                case "Available":
                    java.util.List<Material> available = new ArrayList<>();
                    for (Material m : MatRepo.findAll()) {
                        if (m.getQuantity() > m.getMinQuantity()) {
                            available.add(m);
                        }
                    }
                    resultSet.retainAll(available);
                    break;
            }

            StringBuilder sb = new StringBuilder("Search Results:\n\n");
            for (Material m : resultSet) {
                sb.append(m.toString()).append("\n");
            }

            JOptionPane.showMessageDialog(searchDialog, sb.toString(), "Results", JOptionPane.INFORMATION_MESSAGE);
        });

        searchDialog.setVisible(true);
    }

    private void handleDeleteItem() {
        String name = JOptionPane.showInputDialog(this, "Enter item name to delete:");
        List<Material> matches = MatRepo.findAllByName(name);
        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item not found.");
            return;
        }

        Material material = matches.get(0); // أول تطابق
        MatRepo.remove(material.getId());
        MatRepo.saveToFill();
        JOptionPane.showMessageDialog(this, "Item deleted successfully!");
    }

    private void addItemToRepository(String name, String category, double price ,int quantity, int minQuantity) {
        MatRepo.addMaterial(name ,category , price, quantity , minQuantity);
    }

    private void SaveToCSV()
    {
        MatRepo.saveToFill();
    }

}
// admin123