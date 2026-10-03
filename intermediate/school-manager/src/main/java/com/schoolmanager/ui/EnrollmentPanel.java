package com.schoolmanager.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.AbstractCellEditor;
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
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.schoolmanager.application.EnrollmentApplication;
import com.schoolmanager.model.Enrollment;

public class EnrollmentPanel extends JPanel {

    private static final Color PRIMARY_COLOR = new Color(15, 23, 43);
    private static final Color SECONDARY_TEXT_COLOR = new Color(106, 114, 130);
    private static final Color EDIT_COLOR = new Color(79, 146, 210);
    private static final Color DELETE_COLOR = new Color(255, 74, 67);
    private static final Color WHITE = Color.WHITE;

    private static final Font INTER_REGULAR = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_MEDIUM = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_SEMI_BOLD = new Font("Inter", Font.BOLD, 20);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final EnrollmentApplication enrollmentApplication;
    private final PlaceholderTextField searchField;
    private final JComboBox<String> searchTypeComboBox;
    private final DefaultTableModel tableModel;
    private final JTable enrollmentTable;

    public EnrollmentPanel(EnrollmentApplication enrollmentApplication) {
        this.enrollmentApplication = enrollmentApplication;

        setLayout(new BorderLayout());
        setBackground(WHITE);

        add(createHeader(), BorderLayout.NORTH);

        tableModel = createTableModel();
        enrollmentTable = new JTable(tableModel);

        searchField = new PlaceholderTextField("Search enrollments...");
        searchTypeComboBox = new JComboBox<>(
                new String[] { "ID", "Student ID", "Classroom ID", "Enrolled At" });

        loadEnrollments();
        configureTable();
        configureSearch();

        add(createTableContainer(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 91));
        header.setBorder(BorderFactory.createEmptyBorder(23, 40, 23, 40));
        header.setBackground(WHITE);

        JLabel title = new JLabel("Enrollments");
        title.setForeground(PRIMARY_COLOR);
        title.setFont(INTER_SEMI_BOLD);

        JLabel description = new JLabel("Manage enrollments");
        description.setForeground(SECONDARY_TEXT_COLOR);
        description.setFont(INTER_REGULAR);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(WHITE);
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(description);

        header.add(titlePanel, BorderLayout.WEST);

        JButton addButton = new JButton("Add Enrollment");
        addButton.setPreferredSize(new Dimension(130, 0));
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(WHITE);
        addButton.setFont(INTER_MEDIUM);
        addButton.setFocusPainted(false);
        addButton.setFocusable(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        addButton.addActionListener(_ -> openCreateEnrollmentDialog());

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(addButton);

        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private void openCreateEnrollmentDialog() {
        EnrollmentFormDialog dialog = new EnrollmentFormDialog(
                SwingUtilities.getWindowAncestor(this));

        dialog.setVisible(true);

        Enrollment enrollment = dialog.getEnrollment();
        if (enrollment == null) {
            return;
        }

        try {
            enrollmentApplication.create(enrollment);
            loadEnrollments();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid enrollment",
                    JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException e) {
            showDatabaseError("This enrollment could not be created.");
        }
    }

    private void openEditEnrollmentDialog(int row) {
        Long enrollmentId = (Long) enrollmentTable.getValueAt(row, 0);

        enrollmentApplication.findById(enrollmentId).ifPresent(enrollment -> {
            EnrollmentFormDialog dialog = new EnrollmentFormDialog(
                    SwingUtilities.getWindowAncestor(this),
                    enrollment);

            dialog.setVisible(true);

            Enrollment updatedEnrollment = dialog.getEnrollment();
            if (updatedEnrollment == null) {
                return;
            }

            try {
                enrollmentApplication.update(updatedEnrollment);
                loadEnrollments();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(
                        this,
                        e.getMessage(),
                        "Invalid enrollment",
                        JOptionPane.WARNING_MESSAGE);
            } catch (RuntimeException e) {
                showDatabaseError("This enrollment could not be updated.");
            }
        });
    }

    private void openDeleteEnrollmentDialog(int row) {
        Long enrollmentId = (Long) enrollmentTable.getValueAt(row, 0);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete enrollment " + enrollmentId + "?",
                "Delete Enrollment",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            enrollmentApplication.delete(enrollmentId);
            loadEnrollments();
        } catch (RuntimeException e) {
            showDatabaseError("An unexpected error occurred while deleting the enrollment.");
        }
    }

    private void loadEnrollments() {
        updateTable(enrollmentApplication.findAll());
    }

    private void searchEnrollments() {
        String value = searchField.getText().trim();

        if (value.isEmpty()) {
            loadEnrollments();
            return;
        }

        if ("Enrolled At".equals(searchTypeComboBox.getSelectedItem())) {
            try {
                LocalDate.parse(value, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter the date using the format YYYY-MM-DD.",
                        "Invalid date",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        List<Enrollment> enrollments = enrollmentApplication.search(
                (String) searchTypeComboBox.getSelectedItem(),
                value);

        updateTable(enrollments);
    }

    private void configureSearch() {
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                searchEnrollments();
            }
        });

        searchTypeComboBox.addActionListener(_ -> {
            if (searchField.hasFocus()) {
                searchEnrollments();
            }
        });
    }

    private void updateTable(List<Enrollment> enrollments) {
        tableModel.setRowCount(0);

        for (Enrollment enrollment : enrollments) {
            tableModel.addRow(new Object[] {
                    enrollment.getId(),
                    enrollment.getStudentId(),
                    enrollment.getClassroomId(),
                    enrollment.getEnrolledAt().format(DATE_FORMATTER),
                    ""
            });
        }
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[] { "ID", "Student ID", "Classroom ID", "Enrolled At", "Actions" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
    }

    private JPanel createTableContainer() {
        JPanel container = new JPanel(new BorderLayout(0, 12));
        container.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        container.setBackground(WHITE);

        container.add(createSearchBar(), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
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
        searchTypeComboBox.setPreferredSize(new Dimension(130, 38));
        searchTypeComboBox.setFont(INTER_REGULAR);
        searchTypeComboBox.setBackground(WHITE);
        searchTypeComboBox.setFocusable(false);

        panel.add(searchField, BorderLayout.CENTER);
        panel.add(searchTypeComboBox, BorderLayout.EAST);

        return panel;
    }

    private void configureTable() {
        enrollmentTable.setFont(INTER_MEDIUM);
        enrollmentTable.setForeground(Color.BLACK);
        enrollmentTable.setBackground(WHITE);
        enrollmentTable.setRowHeight(32);
        enrollmentTable.setShowGrid(false);
        enrollmentTable.setIntercellSpacing(new Dimension(0, 0));
        enrollmentTable.setFillsViewportHeight(true);

        configureTableHeader();
        configureCellRenderers();
        configureActionCell();
    }

    private void configureTableHeader() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setBackground(PRIMARY_COLOR);
                setForeground(WHITE);
                setFont(INTER_MEDIUM);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

                return this;
            }
        };

        enrollmentTable.getTableHeader().setDefaultRenderer(renderer);
        enrollmentTable.getTableHeader().setPreferredSize(new Dimension(0, 34));
        enrollmentTable.getTableHeader().setResizingAllowed(false);
        enrollmentTable.getTableHeader().setReorderingAllowed(false);
    }

    private void configureCellRenderers() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setBackground(WHITE);
                setForeground(Color.BLACK);
                setFont(INTER_MEDIUM);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0, new Color(150, 150, 150)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)));

                return this;
            }
        };

        for (int i = 0; i < 5; i++) {
            enrollmentTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void configureActionCell() {
        enrollmentTable.getColumnModel().getColumn(4).setCellRenderer(new ActionRenderer());
        enrollmentTable.getColumnModel().getColumn(4).setCellEditor(new ActionEditor());
    }

    private void showDatabaseError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Enrollment Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private static class ActionRenderer extends JPanel implements TableCellRenderer {

        ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 2));

            add(createButton("Edit", EDIT_COLOR, PRIMARY_COLOR));
            add(createButton("Delete", DELETE_COLOR, WHITE));

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(
                            0, 0, 1, 0, new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        }

        private JButton createButton(String text, Color background, Color foreground) {
            JButton button = new JButton(text);
            button.setBackground(background);
            button.setForeground(foreground);
            button.setFont(INTER_MEDIUM);
            button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setFocusPainted(false);
            button.setFocusable(false);
            return button;
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setBackground(WHITE);
            return this;
        }
    }

    private class ActionEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel;
        private int row;

        ActionEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 2));
            panel.setBackground(WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(
                            0, 0, 1, 0, new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)));

            JButton editButton = createButton("Edit", EDIT_COLOR, PRIMARY_COLOR);
            JButton deleteButton = createButton("Delete", DELETE_COLOR, WHITE);

            editButton.addActionListener(_ -> {
                fireEditingStopped();
                openEditEnrollmentDialog(row);
            });

            deleteButton.addActionListener(_ -> {
                fireEditingStopped();
                openDeleteEnrollmentDialog(row);
            });

            panel.add(editButton);
            panel.add(deleteButton);
        }

        private JButton createButton(String text, Color background, Color foreground) {
            JButton button = new JButton(text);
            button.setBackground(background);
            button.setForeground(foreground);
            button.setFont(INTER_MEDIUM);
            button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setFocusPainted(false);
            button.setFocusable(false);
            return button;
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected,
                int row, int column) {
            this.row = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}
