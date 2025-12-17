package com.factory.ui;

import com.factory.AppContext;
import com.factory.model.ProductLine;
import com.factory.model.Task;
import com.factory.service.AuthService;
import com.factory.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SuperManagerFrame extends JFrame {

    private final AuthService authService;
    private final AppContext ctx;
    private final User user;

    private final DefaultTableModel linesModel = new DefaultTableModel(new Object[]{"ID", "Name", "Active"}, 0);
    private final JTable linesTable = new JTable(linesModel);

    public SuperManagerFrame(User user, AuthService authService, AppContext ctx) {
        this.user = user;
        this.authService = authService;
        this.ctx = ctx;

        setTitle("Super Manager Dashboard - " + user.getUsername());
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        initUI();
        loadLines();
    }

    private void initUI() {

        Color bgDark = new Color(30, 39, 46);
        Color bgPanel = new Color(47, 54, 64);
        Color accent = new Color(52, 152, 219);
        Color textColor = Color.WHITE;

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bgDark);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(24, 30, 36));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("Super Manager Dashboard - " + user.getUsername());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JButton logoutBtn = createFlatButton("Logout", accent);
        logoutBtn.addActionListener(e -> {
            new LoginFrame(authService, ctx).setVisible(true);
            dispose();
        });

        header.add(title, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane();
        split.setDividerLocation(420);
        split.setBackground(bgDark);
        split.setBorder(null);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(bgPanel);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel leftTitle = new JLabel("Production Lines");
        leftTitle.setForeground(textColor);
        leftTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        leftPanel.add(leftTitle, BorderLayout.NORTH);

        styleTable(linesTable, accent);

        JScrollPane scroll = new JScrollPane(linesTable);
        leftPanel.add(scroll, BorderLayout.CENTER);

        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        leftButtons.setOpaque(false);

        JButton addBtn = createFlatButton("Add Line", accent);
        JButton toggleBtn = createFlatButton("Toggle Active", accent);
        JButton refreshBtn = createFlatButton("Refresh", accent);

        leftButtons.add(addBtn);
        leftButtons.add(toggleBtn);
        leftButtons.add(refreshBtn);

        leftPanel.add(leftButtons, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(bgPanel);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel rightTitle = new JLabel("Performance Overview");
        rightTitle.setForeground(textColor);
        rightTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        rightTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        rightPanel.add(rightTitle, BorderLayout.NORTH);

        DefaultTableModel tasksModel = new DefaultTableModel(
                new Object[]{"Task ID", "Product", "Progress %", "Status"},
                0
        );
        JTable tasksTable = new JTable(tasksModel);
        styleTable(tasksTable, accent);

        JScrollPane taskScroll = new JScrollPane(tasksTable);
        rightPanel.add(taskScroll, BorderLayout.CENTER);

        JPanel perfPanel = new JPanel();
        perfPanel.setBackground(bgDark);
        perfPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel rLabel = createLabel("Rating:");
        JLabel nLabel = createLabel("Note:");

        SpinnerNumberModel sm = new SpinnerNumberModel(5, 1, 5, 1);
        JSpinner ratingSpin = new JSpinner(sm);
        JTextField noteField = new JTextField(15);

        JButton submitBtn = createFlatButton("Submit", accent);

        perfPanel.add(rLabel);
        perfPanel.add(ratingSpin);
        perfPanel.add(nLabel);
        perfPanel.add(noteField);
        perfPanel.add(submitBtn);

        rightPanel.add(perfPanel, BorderLayout.SOUTH);

        split.setLeftComponent(leftPanel);
        split.setRightComponent(rightPanel);

        root.add(split, BorderLayout.CENTER);
        add(root);


        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter production line name:");
            if (name != null && !name.trim().isEmpty()) {
                ctx.getProductLineRepository().add(name.trim());
                loadLines();
            }
        });

        toggleBtn.addActionListener(e -> {
            int row = linesTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a line first.");
                return;
            }
            String id = (String) linesModel.getValueAt(row, 0);
            ProductLine pl = ctx.getProductLineRepository().findById(id);
            if (pl != null) {
                pl.setActive(!pl.isActive());
                ctx.getProductLineRepository().update(pl);
                loadLines();
            }
        });

        refreshBtn.addActionListener(e -> loadLines());

        linesTable.getSelectionModel().addListSelectionListener(ev -> {
            tasksModel.setRowCount(0);
            int row = linesTable.getSelectedRow();
            if (row < 0) return;

            String id = (String) linesModel.getValueAt(row, 0);
            List<Task> tasks = ctx.getTaskRepository().findByProductLineId(id);

            for (Task t : tasks) {
                tasksModel.addRow(new Object[]{
                        t.getId(),
                        t.getProductId(),
                        String.format("%.2f", t.getProgressPercent()),
                        t.getStatus()
                });
            }
        });

        submitBtn.addActionListener(e -> {
            int row = linesTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a production line first.");
                return;
            }
            String id = (String) linesModel.getValueAt(row, 0);
            ProductLine pl = ctx.getProductLineRepository().findById(id);
            if (pl == null) return;

            int rating = (Integer) ratingSpin.getValue();
            String note = noteField.getText();

            int tRow = tasksTable.getSelectedRow();
            String taskId = (tRow >= 0) ? (String) tasksModel.getValueAt(tRow, 0) : null;

            pl.addPerformance(taskId, rating, note == null ? "" : note);
            ctx.getProductLineRepository().update(pl);

            JOptionPane.showMessageDialog(this, "Performance Submitted");
            noteField.setText("");
        });
    }

    private JButton createFlatButton(String text, Color accent) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(accent);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }

    private void styleTable(JTable table, Color accent) {
        table.setBackground(new Color(45, 52, 54));
        table.setForeground(Color.WHITE);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(accent);
        table.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    private void loadLines() {
        linesModel.setRowCount(0);
        for (ProductLine pl : ctx.getProductLineRepository().findAll()) {
            linesModel.addRow(new Object[]{pl.getId(), pl.getName(), pl.isActive()});
        }
    }
}
