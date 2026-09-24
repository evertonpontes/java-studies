package com.schoolmanager.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.table.DefaultTableModel;

import com.schoolmanager.application.StudentApplication;
import com.schoolmanager.model.Student;

public class StudentPanel extends JPanel {

    private static final Color PRIMARY_COLOR
            = new Color(15, 23, 43);

    private static final Color SECONDARY_TEXT_COLOR
            = new Color(106, 114, 130);

    private static final Color EDIT_COLOR
            = new Color(79, 146, 210);

    private static final Color DELETE_COLOR
            = new Color(255, 74, 67);

    private static final Color WHITE
            = Color.WHITE;

    private static final Font INTER_REGULAR
            = new Font("Inter", Font.PLAIN, 14);

    private static final Font INTER_MEDIUM
            = new Font("Inter", Font.PLAIN, 14);

    private static final Font INTER_SEMI_BOLD
            = new Font("Inter", Font.BOLD, 20);

    private final StudentApplication studentApplication;

    private final DefaultTableModel tableModel;
    private final JTable studentTable;
    private final DateTimeFormatter dateFormatter
            = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public StudentPanel(
            StudentApplication studentApplication
    ) {
        this.studentApplication = studentApplication;

        setLayout(new BorderLayout());

        setBackground(WHITE);

        add(createHeader(), BorderLayout.NORTH);

        tableModel = createTableModel();

        studentTable = new JTable(tableModel);

        loadStudents();

        add(createTableContainer(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());

        header.setPreferredSize(new Dimension(0, 91));
        header.setBorder(
                BorderFactory.createEmptyBorder(23, 40, 23, 40)
        );

        header.setBackground(WHITE);
        JLabel title = new JLabel("Students");

        title.setForeground(PRIMARY_COLOR);
        title.setFont(INTER_SEMI_BOLD);
        title.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        JLabel description = new JLabel("Manage students");

        description.setForeground(SECONDARY_TEXT_COLOR);
        description.setFont(INTER_REGULAR);
        description.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        titlePanel.setBackground(WHITE);
        titlePanel.add(title, BorderLayout.CENTER);
        title.add(Box.createVerticalStrut(4));
        titlePanel.add(description, BorderLayout.SOUTH);

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
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)
                )
        );

        addButton.setHorizontalAlignment(SwingConstants.CENTER);

        return addButton;
    }

    private void loadStudents() {
        //tableModel.setRowCount(0);

        List<Student> students = studentApplication.findAll();

        for (Student student : students) {
            String birthDate = student.getBirthDate().format(dateFormatter);

            tableModel.addRow(
                    new Object[]{
                        student.getId(),
                        student.getName(),
                        student.getEmail(),
                        birthDate,
                        ""
                    }
            );
        }
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Name",
                    "Email",
                    "Brith Date",
                    "Actions"
                },
                0
        );
    }

    private JPanel createTableContainer() {
        JPanel container = new JPanel(new BorderLayout());

        container.setBorder(
                BorderFactory.createEmptyBorder(0, 40, 0, 40)
        );

        container.setBackground(WHITE);

        JScrollPane scrollPane = new JScrollPane(studentTable);

        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }
}
