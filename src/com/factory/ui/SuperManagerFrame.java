package com.factory.ui;

import com.factory.AppContext;
import com.factory.model.ProductLine;
import com.factory.model.ProductionLineStatus;
import com.factory.model.Task;
import com.factory.service.AuthService;
import com.factory.model.User;
import com.factory.persistence.MaterialRepository;
import com.factory.persistence.ProductRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SuperManagerFrame extends JFrame {

    private final AuthService authService;
    private final AppContext ctx;
    private final User user;

    private final DefaultTableModel linesModel =
            new DefaultTableModel(new Object[]{"ID", "Name", "Status", "Progress %"}, 0);
    private final JTable linesTable = new JTable(linesModel);

    private final DefaultTableModel tasksModel =
            new DefaultTableModel(new Object[]{"Task ID", "Product", "Progress %", "Status"}, 0);
    private final JTable tasksTable = new JTable(tasksModel);

    private final Timer progressTimer = new Timer(true);

    private int selectedLineRow = -1;

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
        startProgressUpdater();
    }

    private void initUI() {
        Color bgDark = new Color(30, 39, 46);
        Color bgPanel = new Color(47, 54, 64);
        Color accent = new Color(52, 152, 219);
        Color danger = new Color(231, 76, 60);

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
        split.setBorder(null);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(bgPanel);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel leftTitle = new JLabel("Production Lines");
        leftTitle.setForeground(Color.WHITE);
        leftTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftPanel.add(leftTitle, BorderLayout.NORTH);

        styleTable(linesTable, accent);
        applyStatusColorRenderer();
        leftPanel.add(new JScrollPane(linesTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setOpaque(false);

        JButton addBtn = createFlatButton("Add", accent);
        JButton changeStatusBtn = createFlatButton("Change Status", accent);
        JButton deleteBtn = createFlatButton("Delete", danger);
        JButton refreshBtn = createFlatButton("Refresh", accent);

        buttons.add(addBtn);
        buttons.add(changeStatusBtn);
        buttons.add(deleteBtn);
        buttons.add(refreshBtn);

        leftPanel.add(buttons, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(bgPanel);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel rightTitle = new JLabel("Performance Overview");
        rightTitle.setForeground(Color.WHITE);
        rightTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        rightTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        rightPanel.add(rightTitle, BorderLayout.NORTH);

        styleTable(tasksTable, accent);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tasksTable.getColumnCount(); i++)
            tasksTable.getColumnModel().getColumn(i).setCellRenderer(center);

        rightPanel.add(new JScrollPane(tasksTable), BorderLayout.CENTER);

        JButton addPerformanceBtn = createFlatButton("Add Performance", accent);
        JButton viewPerformanceBtn = createFlatButton("View Performance", accent);

        JPanel bottomButtonsPanel = new JPanel(new FlowLayout());
        bottomButtonsPanel.setOpaque(false);
        bottomButtonsPanel.add(addPerformanceBtn);
        bottomButtonsPanel.add(viewPerformanceBtn);

        rightPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);

        addPerformanceBtn.addActionListener(e -> addPerformanceAction());
        viewPerformanceBtn.addActionListener(e -> viewPerformanceAction());

        split.setLeftComponent(leftPanel);
        split.setRightComponent(rightPanel);
        root.add(split, BorderLayout.CENTER);

        add(root);

        addBtn.addActionListener(e -> addLineAction());
        changeStatusBtn.addActionListener(e -> changeStatusAction());
        deleteBtn.addActionListener(e -> deleteLineAction());
        refreshBtn.addActionListener(e -> loadLines());

        linesTable.getSelectionModel().addListSelectionListener(e -> {
            selectedLineRow = linesTable.getSelectedRow();
            refreshTasksTable();
        });
    }

    private void startProgressUpdater() {
        progressTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    refreshTasksTable();

                    for (int i = 0; i < linesModel.getRowCount(); i++) {
                        String lineId = ((String) linesModel.getValueAt(i, 0)).trim();
                        List<Task> tasks = ctx.getTaskRepository().findByProductLineId(lineId);
                        double progress = calculateAverageProgress(tasks);
                        linesModel.setValueAt(String.format("%.2f%%", progress), i, 3);
                    }
                });
            }
        }, 0, 1000);
    }

    private void refreshTasksTable() {
        tasksModel.setRowCount(0);
        int row = linesTable.getSelectedRow();
        if (row < 0) return;

        String lineId = ((String) linesModel.getValueAt(row, 0)).trim();
        ProductLine pl = ctx.getProductLineRepository().findById(lineId);
        if (pl == null) return;

        List<Task> tasks = ctx.getTaskRepository().findByProductLineId(lineId);

        ProductRepository productRepo = ctx.getProductRepository();
        MaterialRepository materialRepo = ctx.getMaterialRepository();

        for (Task t : tasks) {
            if (pl.getStatus() == ProductionLineStatus.ACTIVE &&
                    t.getStatus() == Task.Status.IN_PROGRESS) {

                Map<String, Integer> perUnitMaterials = productRepo.getComponentsForProduct(t.getProductId());

                int increment = Math.min(5, t.getTotalQuantity() - t.getCompletedQuantity());

                if (materialRepo.hasEnoughForProduction(perUnitMaterials, increment)) {
                    t.incrementCompleted(increment);
                    materialRepo.consumeForProduction(perUnitMaterials, increment);

                    if (t.getCompletedQuantity() >= t.getTotalQuantity()) {
                        t.setStatus(Task.Status.COMPLETED);
                    }
                } else {
                    t.setStatus(Task.Status.CANCELLED);
                }
            }

            tasksModel.addRow(new Object[]{
                    t.getId(),
                    t.getProductId(),
                    String.format("%.2f", t.getProgressPercent()),
                    t.getStatus()
            });
        }
    }

    private void addPerformanceAction() {
        int row = linesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a Production Line first!",
                    "No Line Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String lineId = (String) linesModel.getValueAt(row, 0);
        ProductLine pl = ctx.getProductLineRepository().findById(lineId);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField ratingField = new JTextField();
        JTextField noteField = new JTextField();
        panel.add(new JLabel("Rating (1-5):"));
        panel.add(ratingField);
        panel.add(new JLabel("Note:"));
        panel.add(noteField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Performance", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int rating = Integer.parseInt(ratingField.getText().trim());
                String note = noteField.getText().trim();
                if (rating < 1 || rating > 5) throw new Exception();

                pl.addPerformance("Line Overall", rating, note);
                ctx.getProductLineRepository().update(pl);

                JOptionPane.showMessageDialog(this, "Performance added successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid rating! Enter 1-5.");
            }
        }
    }

    private void viewPerformanceAction() {
        int row = linesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a Production Line first!",
                    "No Line Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String lineId = (String) linesModel.getValueAt(row, 0);
        ProductLine pl = ctx.getProductLineRepository().findById(lineId);
        if (pl == null) return;

        List<ProductLine.PerformanceEntry> entries = pl.getPerformanceEntries();

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Rating", "Note", "Date"}, 0
        );

        for (ProductLine.PerformanceEntry e : entries) {
            model.addRow(new Object[]{
                    e.getRating(),
                    e.getNote(),
                    e.getCreatedAt()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JButton deleteBtn = new JButton("Delete Selected Note");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(deleteBtn, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(this, "Performance Notes - " + pl.getName(), true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(dialog,
                        "Please select a note to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Delete selected note?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (pl.removePerformanceEntryAt(selectedRow)) {
                    ctx.getProductLineRepository().update(pl);
                    model.removeRow(selectedRow);
                }
            }
        });

        dialog.setVisible(true);
    }


    private void applyStatusColorRenderer() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(CENTER);

                if (value instanceof ProductionLineStatus status) {
                    switch (status) {
                        case ACTIVE -> setForeground(new Color(46, 204, 113));
                        case MAINTENANCE -> setForeground(new Color(241, 196, 15));
                        case STOPPED -> setForeground(new Color(231, 76, 60));
                    }
                } else {
                    setForeground(Color.WHITE);
                }

                return this;
            }
        };

        linesTable.getColumnModel().getColumn(2).setCellRenderer(renderer);
    }

    private JButton createFlatButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    private void styleTable(JTable table, Color accent) {
        table.setBackground(new Color(45, 52, 54));
        table.setForeground(Color.WHITE);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setBackground(accent);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private void loadLines() {
        linesModel.setRowCount(0);
        for (ProductLine pl : ctx.getProductLineRepository().findAll()) {
            List<Task> tasks = ctx.getTaskRepository().findByProductLineId(pl.getId());
            double progress = calculateAverageProgress(tasks);

            linesModel.addRow(new Object[]{
                    pl.getId(),
                    pl.getName(),
                    pl.getStatus(),
                    String.format("%.2f%%", progress)
            });
        }
    }

    private double calculateAverageProgress(List<Task> tasks) {
        if (tasks.isEmpty()) return 0.0;
        double total = 0.0;
        for (Task t : tasks) total += t.getProgressPercent();
        return total / tasks.size();
    }

    private void addLineAction() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField nameField = new JTextField();
        JComboBox<ProductionLineStatus> statusCombo =
                new JComboBox<>(ProductionLineStatus.values());
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add Production Line", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && !nameField.getText().trim().isEmpty()) {
            ctx.getProductLineRepository().add(nameField.getText().trim(),
                    (ProductionLineStatus) statusCombo.getSelectedItem());
            loadLines();
        }
    }

    private void changeStatusAction() {
        int row = linesTable.getSelectedRow();
        if (row < 0) return;

        String id = (String) linesModel.getValueAt(row, 0);
        ProductLine pl = ctx.getProductLineRepository().findById(id);

        ProductionLineStatus newStatus = (ProductionLineStatus) JOptionPane.showInputDialog(
                this,
                "Select new status:",
                "Change Status",
                JOptionPane.PLAIN_MESSAGE,
                null,
                ProductionLineStatus.values(),
                pl.getStatus()
        );

        if (newStatus != null) {
            pl.setStatus(newStatus);
            ctx.getProductLineRepository().update(pl);
            loadLines();
        }
    }

    private void deleteLineAction() {
        int row = linesTable.getSelectedRow();
        if (row < 0) return;
        String id = (String) linesModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete selected production line?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ctx.getProductLineRepository().deleteById(id);
            loadLines();
        }
    }
}
