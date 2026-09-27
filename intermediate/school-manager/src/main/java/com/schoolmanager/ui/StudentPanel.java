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
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.schoolmanager.application.StudentApplication;
import com.schoolmanager.model.Student;

public class StudentPanel extends JPanel {

        private static final Color PRIMARY_COLOR = new Color(15, 23, 43);

        private static final Color SECONDARY_TEXT_COLOR = new Color(106, 114, 130);

        private static final Color EDIT_COLOR = new Color(79, 146, 210);

        private static final Color DELETE_COLOR = new Color(255, 74, 67);

        private static final Color WHITE = Color.WHITE;

        private static final Font INTER_REGULAR = new Font("Inter", Font.PLAIN, 14);

        private static final Font INTER_MEDIUM = new Font("Inter", Font.PLAIN, 14);

        private static final Font INTER_SEMI_BOLD = new Font("Inter", Font.BOLD, 20);

        private final StudentApplication studentApplication;

        private final PlaceholderTextField searchField;
        private final JComboBox<String> searchTypeComboBox;
        private final DefaultTableModel tableModel;
        private final JTable studentTable;
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        public StudentPanel(
                        StudentApplication studentApplication) {
                this.studentApplication = studentApplication;

                setLayout(new BorderLayout());

                setBackground(WHITE);

                add(createHeader(), BorderLayout.NORTH);

                tableModel = createTableModel();

                studentTable = new JTable(tableModel);

                searchField = new PlaceholderTextField("Search students...");
                searchTypeComboBox = new JComboBox<>(
                                new String[] {
                                                "ID",
                                                "Name",
                                                "Email",
                                                "Birth Date"
                                });

                loadStudents();
                configureTable();

                add(createTableContainer(), BorderLayout.CENTER);
        }

        private JPanel createHeader() {
                JPanel header = new JPanel(new BorderLayout());

                header.setPreferredSize(new Dimension(0, 91));
                header.setBorder(
                                BorderFactory.createEmptyBorder(23, 40, 23, 40));

                header.setBackground(WHITE);
                JLabel title = new JLabel("Students");

                title.setForeground(PRIMARY_COLOR);
                title.setFont(INTER_SEMI_BOLD);
                title.setHorizontalAlignment(
                                SwingConstants.LEFT);

                JLabel description = new JLabel("Manage students");

                description.setForeground(SECONDARY_TEXT_COLOR);
                description.setFont(INTER_REGULAR);
                description.setHorizontalAlignment(
                                SwingConstants.LEFT);

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
                JButton addButton = new JButton("Add Student");

                addButton.setPreferredSize(new Dimension(113, 0));
                addButton.setBackground(PRIMARY_COLOR);
                addButton.setForeground(WHITE);

                addButton.setFont(INTER_MEDIUM);
                addButton.setFocusPainted(false);
                addButton.setFocusable(false);

                addButton.setBorder(
                                BorderFactory.createCompoundBorder(
                                                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                                                BorderFactory.createEmptyBorder(0, 10, 0, 10)));

                addButton.setHorizontalAlignment(SwingConstants.CENTER);
                addButton.addActionListener(event -> openCreateStudentDialog());

                return addButton;
        }

        private void openCreateStudentDialog() {
                StudentFormDialog dialog = new StudentFormDialog(
                                javax.swing.SwingUtilities.getWindowAncestor(this));

                dialog.setVisible(true);

                Student student = dialog.getStudent();

                if (student == null) {
                        return;
                }

                studentApplication.create(student);
                loadStudents();
        }

        private void openEditStudentDialog(int row) {
                Long studentId = (Long) studentTable.getValueAt(row, 0);

                studentApplication.findById(studentId).ifPresent(student -> {
                        StudentFormDialog dialog = new StudentFormDialog(
                                        javax.swing.SwingUtilities.getWindowAncestor(this),
                                        student);

                        dialog.setVisible(true);

                        Student updatedStudent = dialog.getStudent();

                        if (updatedStudent == null) {
                                return;
                        }

                        studentApplication.update(updatedStudent);
                        loadStudents();
                });
        }

        private void openDeleteStudentDialog(int row) {
                Long studentId = (Long) studentTable.getValueAt(row, 0);
                String studentName = (String) studentTable.getValueAt(row, 1);

                int result = JOptionPane.showConfirmDialog(
                                this,
                                "Are you sure you want to delete " + studentName + "?",
                                "Delete Student",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);

                if (result != JOptionPane.YES_OPTION) {
                        return;
                }

                try {
                        studentApplication.delete(studentId);
                        loadStudents();

                } catch (RuntimeException e) {

                        if (hasIntegrityConstraintViolation(e)) {
                                JOptionPane.showMessageDialog(this, """
                                                This student cannot be deleted because they have enrollments.

                                                Please delete the student's enrollments before deleting the student.""",
                                                "Cannot Delete Student",
                                                JOptionPane.WARNING_MESSAGE);
                        } else {
                                JOptionPane.showMessageDialog(
                                                this,
                                                "An unexpected error occurred while deleting the student.",
                                                "Delete Student",
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

        private void loadStudents() {
                List<Student> students = studentApplication.findAll();

                updateTable(students);
        }

        private void updateTable(List<Student> students) {
                tableModel.setRowCount(0);

                for (Student student : students) {

                        String birthDate = student.getBirthDate()
                                        .format(dateFormatter);

                        tableModel.addRow(
                                        new Object[] {
                                                        student.getId(),
                                                        student.getName(),
                                                        student.getEmail(),
                                                        birthDate,
                                                        ""
                                        });
                }
        }

        private DefaultTableModel createTableModel() {
                return new DefaultTableModel(
                                new Object[] {
                                                "ID",
                                                "Name",
                                                "Email",
                                                "Brith Date",
                                                "Actions"
                                },
                                0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return column == 4;
                        }

                };
        }

        private JPanel createTableContainer() {
                JPanel container = new JPanel(new BorderLayout(0, 12));

                container.setBorder(
                                BorderFactory.createEmptyBorder(0, 40, 0, 40));

                container.setBackground(WHITE);

                container.add(createSearchBar(), BorderLayout.NORTH);

                JScrollPane scrollPane = new JScrollPane(studentTable);

                scrollPane.setBackground(WHITE);
                scrollPane.getViewport().setBackground(WHITE);

                scrollPane.setBorder(
                                BorderFactory.createEmptyBorder());

                container.add(scrollPane, BorderLayout.CENTER);

                return container;
        }

        private JPanel createSearchBar() {
                JPanel panel = new JPanel(new BorderLayout(8, 0));

                panel.setBackground(WHITE);

                searchField.setPreferredSize(new Dimension(0, 38));

                searchField.setFont(INTER_REGULAR);

                searchField.setBorder(
                                BorderFactory.createCompoundBorder(
                                                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                                                BorderFactory.createEmptyBorder(
                                                                0, 10, 0, 10)));

                searchTypeComboBox.setUI(new BasicComboBoxUI() {

                        @Override
                        protected JButton createArrowButton() {
                                JButton b = new JButton("▼");

                                b.setBackground(WHITE);
                                b.setForeground(new Color(161, 161, 161));
                                b.setBorder(
                                                BorderFactory.createEmptyBorder());

                                b.setFocusPainted(false);
                                b.setFocusable(false);

                                return b;
                        }
                });

                searchTypeComboBox.setBorder(
                                BorderFactory.createCompoundBorder(
                                                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                                                BorderFactory.createEmptyBorder(0, 8, 0, 0)));

                searchTypeComboBox.setPreferredSize(
                                new Dimension(120, 38));

                searchTypeComboBox.setFont(INTER_REGULAR);
                searchTypeComboBox.setBackground(WHITE);
                searchTypeComboBox.setFocusable(false);

                panel.add(searchField, BorderLayout.CENTER);
                panel.add(searchTypeComboBox, BorderLayout.EAST);

                searchField.addFocusListener(new FocusAdapter() {

                        @Override
                        public void focusLost(FocusEvent e) {
                                searchStudents();
                        }
                });

                searchTypeComboBox.addActionListener(
                                _ -> searchStudents());

                return panel;
        }

        private void searchStudents() {
                String value = searchField.getText().trim();

                if (value.isEmpty()) {
                        loadStudents();
                        return;
                }

                String field = (String) searchTypeComboBox.getSelectedItem();

                try {
                        List<Student> students = studentApplication.search(field, value);

                        updateTable(students);

                } catch (NumberFormatException e) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "ID must contain only numbers.",
                                        "Invalid search",
                                        JOptionPane.WARNING_MESSAGE);

                } catch (DateTimeParseException e) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Birth date must use the format yyyy/MM/dd.",
                                        "Invalid search",
                                        JOptionPane.WARNING_MESSAGE);
                }
        }

        private void configureTable() {
                studentTable.setFont(INTER_MEDIUM);
                studentTable.setForeground(Color.BLACK);
                studentTable.setBackground(WHITE);

                studentTable.setRowHeight(32);
                studentTable.setShowGrid(false);
                studentTable.setIntercellSpacing(new Dimension(0, 0));

                studentTable.setFillsViewportHeight(true);

                configureTableHeader();

                configureCellRenderers();

                configureActionCell();
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
                                                table,
                                                value,
                                                isSelected,
                                                hasFocus,
                                                row,
                                                column);

                                setBackground(PRIMARY_COLOR);
                                setForeground(WHITE);
                                setFont(INTER_MEDIUM);

                                setBorder(
                                                BorderFactory.createEmptyBorder(
                                                                0, 8, 0, 8));

                                return component;
                        }
                };

                studentTable.getTableHeader().setDefaultRenderer(renderer);

                studentTable.getTableHeader().setPreferredSize(
                                new Dimension(0, 34));

                studentTable.getTableHeader().setResizingAllowed(false);
                studentTable.getTableHeader().setReorderingAllowed(false);
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
                                                table,
                                                value,
                                                isSelected,
                                                hasFocus,
                                                row,
                                                column);

                                setBackground(WHITE);
                                setForeground(Color.BLACK);
                                setFont(INTER_MEDIUM);
                                setBorder(
                                                BorderFactory.createCompoundBorder(
                                                                BorderFactory.createMatteBorder(0, 0, 1, 0,
                                                                                new Color(150, 150, 150)),
                                                                BorderFactory.createEmptyBorder(
                                                                                8, 8, 8, 8)));

                                return component;
                        }
                };

                for (int i = 0; i < 5; i++) {
                        studentTable.getColumnModel()
                                        .getColumn(i)
                                        .setCellRenderer(renderer);
                }
        }

        private void configureActionCell() {
                studentTable.getColumnModel()
                                .getColumn(4)
                                .setCellRenderer(new ActionRenderer());

                studentTable.getColumnModel()
                                .getColumn(4)
                                .setCellEditor(new ActionEditor());
        }

        private static class ActionRenderer extends JPanel implements TableCellRenderer {

                public ActionRenderer() {
                        setLayout(
                                        new FlowLayout(
                                                        FlowLayout.RIGHT,
                                                        3,
                                                        2));

                        JButton editButton = createButton(
                                        "Edit",
                                        EDIT_COLOR);

                        JButton deleteButton = createButton(
                                        "Delete",
                                        DELETE_COLOR);

                        add(editButton);
                        add(deleteButton);

                        setBorder(
                                        BorderFactory.createCompoundBorder(
                                                        BorderFactory.createMatteBorder(0, 0, 1, 0,
                                                                        new Color(150, 150, 150)),
                                                        BorderFactory.createEmptyBorder(
                                                                        4, 4, 4, 4)));

                }

                private JButton createButton(String text, Color backgroundColor) {
                        JButton button = new JButton(text);

                        button.setBackground(backgroundColor);
                        button.setFont(INTER_MEDIUM);

                        button.setForeground(
                                        text.equals("Delete") ? WHITE
                                                        : PRIMARY_COLOR);

                        button.setBorder(
                                        BorderFactory.createCompoundBorder(
                                                        BorderFactory.createEmptyBorder(0, 10, 0, 10),
                                                        BorderFactory.createEmptyBorder(0, 0, 0, 0)));

                        button.setMargin(
                                        new Insets(0, 0, 0, 0));

                        button.setFocusPainted(false);
                        button.setFocusable(false);

                        return button;
                }

                @Override
                public Component getTableCellRendererComponent(
                                JTable table,
                                Object value,
                                boolean isSelected,
                                boolean hasFocus,
                                int row,
                                int column) {

                        setBackground(WHITE);

                        return this;
                }

        }

        private class ActionEditor extends AbstractCellEditor implements TableCellEditor {

                private final JPanel panel;
                private int row;

                public ActionEditor() {
                        panel = new JPanel(
                                        new FlowLayout(
                                                        FlowLayout.RIGHT,
                                                        3,
                                                        2));

                        panel.setBackground(WHITE);
                        panel.setBorder(
                                        BorderFactory.createCompoundBorder(
                                                        BorderFactory.createMatteBorder(0, 0, 1, 0,
                                                                        new Color(150, 150, 150)),
                                                        BorderFactory.createEmptyBorder(
                                                                        4, 4, 4, 4)));

                        JButton editButton = createButton("Edit", EDIT_COLOR, PRIMARY_COLOR);
                        JButton deleteButton = createButton("Delete", DELETE_COLOR, WHITE);

                        editButton.addActionListener(_ -> {
                                fireEditingStopped();
                                openEditStudentDialog(row);
                        });

                        deleteButton.addActionListener(_ -> {
                                fireEditingStopped();
                                openDeleteStudentDialog(row);
                        });

                        panel.add(editButton);
                        panel.add(deleteButton);
                }

                private JButton createButton(String text, Color backgroundColor, Color foregroundColor) {
                        JButton button = new JButton(text);

                        button.setBackground(backgroundColor);
                        button.setForeground(foregroundColor);
                        button.setFont(INTER_MEDIUM);
                        button.setBorder(
                                        BorderFactory.createCompoundBorder(
                                                        BorderFactory.createEmptyBorder(0, 10, 0, 10),
                                                        BorderFactory.createEmptyBorder(0, 0, 0, 0)));
                        button.setMargin(new Insets(0, 0, 0, 0));
                        button.setFocusPainted(false);
                        button.setFocusable(false);

                        return button;
                }

                @Override
                public Component getTableCellEditorComponent(
                                JTable table,
                                Object value,
                                boolean isSelected,
                                int row,
                                int column) {
                        this.row = row;
                        return panel;
                }

                @Override
                public Object getCellEditorValue() {
                        return "";
                }
        }
}
