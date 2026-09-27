package com.schoolmanager.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.schoolmanager.application.TeacherApplication;
import com.schoolmanager.model.Teacher;

public class TeacherPanel extends JPanel {

    private static final Color PRIMARY_COLOR = new Color(15, 23, 43);
    private static final Color SECONDARY_TEXT_COLOR = new Color(106, 114, 130);
    private static final Color WHITE = Color.WHITE;

    private static final Font INTER_REGULAR = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_MEDIUM = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_SEMI_BOLD = new Font("Inter", Font.BOLD, 20);

    private final TeacherApplication teacherApplication;

    private final PlaceholderTextField searchField;
    private final JComboBox<String> searchTypeComboBox;
    private final DefaultTableModel tableModel;
    private final JTable teacherTable;

    public TeacherPanel(TeacherApplication teacherApplication) {
        this.teacherApplication = teacherApplication;

        setLayout(new BorderLayout());
        setBackground(WHITE);

        add(createHeader(), BorderLayout.NORTH);

        tableModel = createTableModel();
        teacherTable = new JTable(tableModel);

        searchField = new PlaceholderTextField("Search teachers...");
        searchTypeComboBox = new JComboBox<>(new String[] { "ID", "Name", "Email" });

        loadTeachers();
        configureTable();

        add(createTableContainer(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());

        header.setPreferredSize(new Dimension(0, 91));
        header.setBorder(BorderFactory.createEmptyBorder(23, 40, 23, 40));
        header.setBackground(WHITE);

        JLabel title = new JLabel("Teachers");
        title.setForeground(PRIMARY_COLOR);
        title.setFont(INTER_SEMI_BOLD);
        title.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel description = new JLabel("Manage teachers");
        description.setForeground(SECONDARY_TEXT_COLOR);
        description.setFont(INTER_REGULAR);
        description.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(WHITE);
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(description);

        header.add(titlePanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(createAddButton());
        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private JButton createAddButton() {
        JButton addButton = new JButton("Add Teacher");

        addButton.setPreferredSize(new Dimension(113, 0));
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(WHITE);
        addButton.setFont(INTER_MEDIUM);
        addButton.setFocusPainted(false);
        addButton.setFocusable(false);
        addButton.setEnabled(false);
        addButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        addButton.setHorizontalAlignment(SwingConstants.CENTER);

        return addButton;
    }

    private void loadTeachers() {
        List<Teacher> teachers = teacherApplication.findAll();
        updateTable(teachers);
    }

    private void updateTable(List<Teacher> teachers) {
        tableModel.setRowCount(0);

        for (Teacher teacher : teachers) {
            tableModel.addRow(new Object[] {
                    teacher.getId(),
                    teacher.getName(),
                    teacher.getEmail()
            });
        }
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[] { "ID", "Name", "Email" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JPanel createTableContainer() {
        JPanel container = new JPanel(new BorderLayout(0, 12));
        container.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        container.setBackground(WHITE);

        container.add(createSearchBar(), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(teacherTable);
        scrollPane.setBackground(WHITE);
        scrollPane.getViewport().setBackground(WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    private JPanel createSearchBar() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(WHITE);

        searchField.setPreferredSize(new Dimension(0, 38));
        searchField.setFont(INTER_REGULAR);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));

        searchTypeComboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setBackground(WHITE);
                button.setForeground(new Color(161, 161, 161));
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setFocusPainted(false);
                button.setFocusable(false);
                return button;
            }
        });

        searchTypeComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(0, 8, 0, 0)));
        searchTypeComboBox.setPreferredSize(new Dimension(120, 38));
        searchTypeComboBox.setFont(INTER_REGULAR);
        searchTypeComboBox.setBackground(WHITE);
        searchTypeComboBox.setFocusable(false);

        panel.add(searchField, BorderLayout.CENTER);
        panel.add(searchTypeComboBox, BorderLayout.EAST);

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                searchTeachers();
            }
        });

        searchTypeComboBox.addActionListener(_ -> searchTeachers());

        return panel;
    }

    private void searchTeachers() {
        String value = searchField.getText().trim();

        if (value.isEmpty()) {
            loadTeachers();
            return;
        }

        String field = (String) searchTypeComboBox.getSelectedItem();

        try {
            List<Teacher> teachers = teacherApplication.search(field, value);
            updateTable(teachers);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "ID must contain only numbers.",
                    "Invalid search",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void configureTable() {
        teacherTable.setFont(INTER_MEDIUM);
        teacherTable.setForeground(Color.BLACK);
        teacherTable.setBackground(WHITE);
        teacherTable.setRowHeight(32);
        teacherTable.setShowGrid(false);
        teacherTable.setIntercellSpacing(new Dimension(0, 0));
        teacherTable.setFillsViewportHeight(true);

        configureTableHeader();
        configureCellRenderers();
    }

    private void configureTableHeader() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setBackground(PRIMARY_COLOR);
                setForeground(WHITE);
                setFont(INTER_MEDIUM);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

                return component;
            }
        };

        teacherTable.getTableHeader().setDefaultRenderer(renderer);
        teacherTable.getTableHeader().setPreferredSize(new Dimension(0, 34));
        teacherTable.getTableHeader().setResizingAllowed(false);
        teacherTable.getTableHeader().setReorderingAllowed(false);
    }

    private void configureCellRenderers() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setBackground(WHITE);
                setForeground(Color.BLACK);
                setFont(INTER_MEDIUM);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0, new Color(150, 150, 150)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)));

                return component;
            }
        };

        for (int i = 0; i < teacherTable.getColumnCount(); i++) {
            teacherTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }
}
