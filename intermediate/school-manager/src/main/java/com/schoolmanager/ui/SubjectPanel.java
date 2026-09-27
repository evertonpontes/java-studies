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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.schoolmanager.application.SubjectApplication;
import com.schoolmanager.model.Subject;

public class SubjectPanel extends JPanel {

    private static final Color PRIMARY_COLOR = new Color(15, 23, 43);
    private static final Color SECONDARY_TEXT_COLOR = new Color(106, 114, 130);
    private static final Color EDIT_COLOR = new Color(79, 146, 210);
    private static final Color DELETE_COLOR = new Color(255, 74, 67);
    private static final Color WHITE = Color.WHITE;

    private static final Font INTER_REGULAR = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_MEDIUM = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_SEMI_BOLD = new Font("Inter", Font.BOLD, 20);

    private final SubjectApplication subjectApplication;
    private final PlaceholderTextField searchField;
    private final JComboBox<String> searchTypeComboBox;
    private final DefaultTableModel tableModel;
    private final JTable subjectTable;

    public SubjectPanel(SubjectApplication subjectApplication) {
        this.subjectApplication = subjectApplication;

        setLayout(new BorderLayout());
        setBackground(WHITE);

        add(createHeader(), BorderLayout.NORTH);

        tableModel = createTableModel();
        subjectTable = new JTable(tableModel);

        searchField = new PlaceholderTextField("Search subjects...");
        searchTypeComboBox = new JComboBox<>(new String[] { "ID", "Name" });

        loadSubjects();
        configureTable();
        configureSearch();

        add(createTableContainer(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 91));
        header.setBorder(BorderFactory.createEmptyBorder(23, 40, 23, 40));
        header.setBackground(WHITE);

        JLabel title = new JLabel("Subjects");
        title.setForeground(PRIMARY_COLOR);
        title.setFont(INTER_SEMI_BOLD);

        JLabel description = new JLabel("Manage subjects");
        description.setForeground(SECONDARY_TEXT_COLOR);
        description.setFont(INTER_REGULAR);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(WHITE);
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(description);

        header.add(titlePanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(WHITE);

        JButton addButton = new JButton("Add Subject");
        addButton.setPreferredSize(new Dimension(113, 0));
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(WHITE);
        addButton.setFont(INTER_MEDIUM);
        addButton.setFocusPainted(false);
        addButton.setFocusable(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        addButton.addActionListener(_ -> openCreateSubjectDialog());

        buttonPanel.add(addButton);
        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private void openCreateSubjectDialog() {
        SubjectFormDialog dialog = new SubjectFormDialog(
                SwingUtilities.getWindowAncestor(this));

        dialog.setVisible(true);

        Subject subject = dialog.getSubject();
        if (subject == null) {
            return;
        }

        subjectApplication.create(subject);
        loadSubjects();
    }

    private void openEditSubjectDialog(int row) {
        Long subjectId = (Long) subjectTable.getValueAt(row, 0);

        subjectApplication.findById(subjectId).ifPresent(subject -> {
            SubjectFormDialog dialog = new SubjectFormDialog(
                    SwingUtilities.getWindowAncestor(this),
                    subject);

            dialog.setVisible(true);

            Subject updatedSubject = dialog.getSubject();
            if (updatedSubject == null) {
                return;
            }

            subjectApplication.update(updatedSubject);
            loadSubjects();
        });
    }

    private void openDeleteSubjectDialog(int row) {
        Long subjectId = (Long) subjectTable.getValueAt(row, 0);
        String subjectName = (String) subjectTable.getValueAt(row, 1);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + subjectName + "?",
                "Delete Subject",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            subjectApplication.delete(subjectId);
            loadSubjects();
        } catch (RuntimeException e) {
            if (hasIntegrityConstraintViolation(e)) {
                JOptionPane.showMessageDialog(
                        this,
                        "This subject cannot be deleted because it has related records.\n\n"
                                + "Please remove the related records before deleting the subject.",
                        "Cannot Delete Subject",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "An unexpected error occurred while deleting the subject.",
                        "Delete Subject",
                        JOptionPane.ERROR_MESSAGE);
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

    private void loadSubjects() {
        updateTable(subjectApplication.findAll());
    }

    private void searchSubjects() {
        String value = searchField.getText().trim();

        if (value.isEmpty()) {
            loadSubjects();
            return;
        }

        List<Subject> subjects = subjectApplication.search(
                (String) searchTypeComboBox.getSelectedItem(),
                value);

        updateTable(subjects);
    }

    private void configureSearch() {
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                searchSubjects();
            }
        });

        searchTypeComboBox.addActionListener(_ -> {
            if (searchField.hasFocus()) {
                searchSubjects();
            }
        });
    }

    private void updateTable(List<Subject> subjects) {
        tableModel.setRowCount(0);

        for (Subject subject : subjects) {
            tableModel.addRow(new Object[] {
                    subject.getId(),
                    subject.getName(),
                    subject.getDescription(),
                    ""
            });
        }
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[] { "ID", "Name", "Description", "Actions" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
    }

    private JPanel createTableContainer() {
        JPanel container = new JPanel(new BorderLayout(0, 12));
        container.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        container.setBackground(WHITE);

        container.add(createSearchBar(), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(subjectTable);
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

        return panel;
    }

    private void configureTable() {
        subjectTable.setFont(INTER_MEDIUM);
        subjectTable.setForeground(Color.BLACK);
        subjectTable.setBackground(WHITE);
        subjectTable.setRowHeight(32);
        subjectTable.setShowGrid(false);
        subjectTable.setIntercellSpacing(new Dimension(0, 0));
        subjectTable.setFillsViewportHeight(true);

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

        subjectTable.getTableHeader().setDefaultRenderer(renderer);
        subjectTable.getTableHeader().setPreferredSize(new Dimension(0, 34));
        subjectTable.getTableHeader().setResizingAllowed(false);
        subjectTable.getTableHeader().setReorderingAllowed(false);
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

        for (int i = 0; i < 4; i++) {
            subjectTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void configureActionCell() {
        subjectTable.getColumnModel().getColumn(3).setCellRenderer(new ActionRenderer());
        subjectTable.getColumnModel().getColumn(3).setCellEditor(new ActionEditor());
    }

    private static class ActionRenderer extends JPanel implements TableCellRenderer {
        public ActionRenderer() {
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
                openEditSubjectDialog(row);
            });

            deleteButton.addActionListener(_ -> {
                fireEditingStopped();
                openDeleteSubjectDialog(row);
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
