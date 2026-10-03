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

import com.schoolmanager.application.ClassroomApplication;
import com.schoolmanager.model.Classroom;

public class ClassroomPanel extends JPanel {

    private static final Color PRIMARY_COLOR = new Color(15, 23, 43);
    private static final Color SECONDARY_TEXT_COLOR = new Color(106, 114, 130);
    private static final Color EDIT_COLOR = new Color(79, 146, 210);
    private static final Color DELETE_COLOR = new Color(255, 74, 67);
    private static final Color WHITE = Color.WHITE;

    private static final Font INTER_REGULAR = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_MEDIUM = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_SEMI_BOLD = new Font("Inter", Font.BOLD, 20);

    private final ClassroomApplication classroomApplication;
    private final PlaceholderTextField searchField;
    private final JComboBox<String> searchTypeComboBox;
    private final DefaultTableModel tableModel;
    private final JTable classroomTable;

    public ClassroomPanel(ClassroomApplication classroomApplication) {
        this.classroomApplication = classroomApplication;

        setLayout(new BorderLayout());
        setBackground(WHITE);

        add(createHeader(), BorderLayout.NORTH);

        tableModel = createTableModel();
        classroomTable = new JTable(tableModel);

        searchField = new PlaceholderTextField("Search classrooms...");
        searchTypeComboBox = new JComboBox<>(
                new String[] { "ID", "Name", "Teacher ID", "Subject ID" });

        loadClassrooms();
        configureTable();
        configureSearch();

        add(createTableContainer(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 91));
        header.setBorder(BorderFactory.createEmptyBorder(23, 40, 23, 40));
        header.setBackground(WHITE);

        JLabel title = new JLabel("Classrooms");
        title.setForeground(PRIMARY_COLOR);
        title.setFont(INTER_SEMI_BOLD);

        JLabel description = new JLabel("Manage classrooms");
        description.setForeground(SECONDARY_TEXT_COLOR);
        description.setFont(INTER_REGULAR);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(WHITE);
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(description);

        header.add(titlePanel, BorderLayout.WEST);

        JButton addButton = new JButton("Add Classroom");
        addButton.setPreferredSize(new Dimension(125, 0));
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(WHITE);
        addButton.setFont(INTER_MEDIUM);
        addButton.setFocusPainted(false);
        addButton.setFocusable(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        addButton.addActionListener(_ -> openCreateClassroomDialog());

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(addButton);

        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private void openCreateClassroomDialog() {
        ClassroomFormDialog dialog = new ClassroomFormDialog(
                SwingUtilities.getWindowAncestor(this));

        dialog.setVisible(true);

        Classroom classroom = dialog.getClassroom();
        if (classroom == null) {
            return;
        }

        try {
            classroomApplication.create(classroom);
            loadClassrooms();
        } catch (RuntimeException e) {
            showDatabaseError("This classroom could not be created.");
        }
    }

    private void openEditClassroomDialog(int row) {
        Long classroomId = (Long) classroomTable.getValueAt(row, 0);

        classroomApplication.findById(classroomId).ifPresent(classroom -> {
            ClassroomFormDialog dialog = new ClassroomFormDialog(
                    SwingUtilities.getWindowAncestor(this),
                    classroom);

            dialog.setVisible(true);

            Classroom updatedClassroom = dialog.getClassroom();
            if (updatedClassroom == null) {
                return;
            }

            try {
                classroomApplication.update(updatedClassroom);
                loadClassrooms();
            } catch (RuntimeException e) {
                showDatabaseError("This classroom could not be updated.");
            }
        });
    }

    private void openDeleteClassroomDialog(int row) {
        Long classroomId = (Long) classroomTable.getValueAt(row, 0);
        String classroomName = (String) classroomTable.getValueAt(row, 1);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + classroomName + "?",
                "Delete Classroom",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            classroomApplication.delete(classroomId);
            loadClassrooms();
        } catch (RuntimeException e) {
            if (hasIntegrityConstraintViolation(e)) {
                JOptionPane.showMessageDialog(
                        this,
                        "This classroom cannot be deleted because it has related records.\n\n"
                                + "Please remove the related records before deleting the classroom.",
                        "Cannot Delete Classroom",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                showDatabaseError("An unexpected error occurred while deleting the classroom.");
            }
        }
    }

    private boolean hasIntegrityConstraintViolation(Throwable throwable) {
        Throwable cause = throwable;

        while (cause != null) {
            if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
                return true;
            }
            cause = cause.getCause();
        }

        return false;
    }

    private void loadClassrooms() {
        updateTable(classroomApplication.findAll());
    }

    private void searchClassrooms() {
        String value = searchField.getText().trim();

        if (value.isEmpty()) {
            loadClassrooms();
            return;
        }

        List<Classroom> classrooms = classroomApplication.search(
                (String) searchTypeComboBox.getSelectedItem(),
                value);

        updateTable(classrooms);
    }

    private void configureSearch() {
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                searchClassrooms();
            }
        });

        searchTypeComboBox.addActionListener(_ -> {
            if (searchField.hasFocus()) {
                searchClassrooms();
            }
        });
    }

    private void updateTable(List<Classroom> classrooms) {
        tableModel.setRowCount(0);

        for (Classroom classroom : classrooms) {
            tableModel.addRow(new Object[] {
                    classroom.getId(),
                    classroom.getName(),
                    classroom.getTeacherId(),
                    classroom.getSubjectId(),
                    ""
            });
        }
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[] { "ID", "Name", "Teacher ID", "Subject ID", "Actions" },
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

        JScrollPane scrollPane = new JScrollPane(classroomTable);
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
        classroomTable.setFont(INTER_MEDIUM);
        classroomTable.setForeground(Color.BLACK);
        classroomTable.setBackground(WHITE);
        classroomTable.setRowHeight(32);
        classroomTable.setShowGrid(false);
        classroomTable.setIntercellSpacing(new Dimension(0, 0));
        classroomTable.setFillsViewportHeight(true);

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

        classroomTable.getTableHeader().setDefaultRenderer(renderer);
        classroomTable.getTableHeader().setPreferredSize(new Dimension(0, 34));
        classroomTable.getTableHeader().setResizingAllowed(false);
        classroomTable.getTableHeader().setReorderingAllowed(false);
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
            classroomTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void configureActionCell() {
        classroomTable.getColumnModel().getColumn(4).setCellRenderer(new ActionRenderer());
        classroomTable.getColumnModel().getColumn(4).setCellEditor(new ActionEditor());
    }

    private void showDatabaseError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Classroom Error",
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
                openEditClassroomDialog(row);
            });

            deleteButton.addActionListener(_ -> {
                fireEditingStopped();
                openDeleteClassroomDialog(row);
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
