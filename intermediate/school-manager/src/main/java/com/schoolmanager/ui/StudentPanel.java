package com.schoolmanager.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
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

        private void loadStudents() {
                tableModel.setRowCount(0);

                List<Student> students = studentApplication.findAll();

                for (Student student : students) {
                        String birthDate = student.getBirthDate().format(dateFormatter);

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
                                return false;
                        }

                };
        }

        private JPanel createTableContainer() {
                JPanel container = new JPanel(new BorderLayout());

                container.setBorder(
                                BorderFactory.createEmptyBorder(0, 40, 0, 40));

                container.setBackground(WHITE);

                JScrollPane scrollPane = new JScrollPane(studentTable);

                scrollPane.setBackground(WHITE);
                scrollPane.getViewport().setBackground(WHITE);

                scrollPane.setBorder(
                                BorderFactory.createEmptyBorder());

                container.add(scrollPane, BorderLayout.CENTER);

                return container;
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
                                                                                new Color(161, 161, 161)),
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
        }

        private class ActionRenderer extends JPanel implements TableCellRenderer {

                JButton editButton;
                JButton deleteButton;

                public ActionRenderer() {
                        setLayout(
                                        new FlowLayout(
                                                        FlowLayout.RIGHT,
                                                        3,
                                                        2));

                        editButton = createButton(
                                        "Edit",
                                        EDIT_COLOR);

                        deleteButton = createButton(
                                        "Delete",
                                        DELETE_COLOR);

                        add(editButton);
                        add(deleteButton);

                        setBorder(
                                        BorderFactory.createCompoundBorder(
                                                        BorderFactory.createMatteBorder(0, 0, 1, 0,
                                                                        new Color(161, 161, 161)),
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
}
