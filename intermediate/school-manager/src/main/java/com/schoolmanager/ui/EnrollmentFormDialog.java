package com.schoolmanager.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.schoolmanager.model.Enrollment;

public class EnrollmentFormDialog extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(15, 23, 43);
    private static final Color SECONDARY_TEXT_COLOR = new Color(106, 114, 130);
    private static final Color WHITE = Color.WHITE;

    private static final Font INTER_REGULAR = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_MEDIUM = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_SEMI_BOLD = new Font("Inter", Font.BOLD, 20);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final PlaceholderTextField studentIdField;
    private final PlaceholderTextField classroomIdField;

    private final boolean editMode;
    private final Enrollment originalEnrollment;

    private Enrollment enrollment;

    public EnrollmentFormDialog(Window owner) {
        this(owner, null);
    }

    public EnrollmentFormDialog(Window owner, Enrollment enrollment) {
        super(owner, enrollment == null ? "Add Enrollment" : "Edit Enrollment", ModalityType.APPLICATION_MODAL);

        this.editMode = enrollment != null;
        this.originalEnrollment = enrollment;

        studentIdField = new PlaceholderTextField("Enter student ID");
        classroomIdField = new PlaceholderTextField("Enter classroom ID");

        configureDialog();
        createContent();

        if (editMode) {
            fillForm(enrollment);
        }
    }

    private void configureDialog() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(540, 380);
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void createContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 20, 28));

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createForm(), BorderLayout.CENTER);
        root.add(createActions(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel(editMode ? "Edit Enrollment" : "Add Enrollment");
        title.setFont(INTER_SEMI_BOLD);
        title.setForeground(PRIMARY_COLOR);

        JLabel description = new JLabel(
                editMode ? "Update the enrollment's information" : "Enter the enrollment's information");
        description.setFont(INTER_REGULAR);
        description.setForeground(SECONDARY_TEXT_COLOR);

        JPanel textPanel = new JPanel(new BorderLayout(0, 4));
        textPanel.setBackground(WHITE);
        textPanel.add(title, BorderLayout.NORTH);
        textPanel.add(description, BorderLayout.SOUTH);

        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(WHITE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = new Insets(0, 0, 12, 0);

        addField(panel, constraints, 0, "Student ID", studentIdField);
        addField(panel, constraints, 2, "Classroom ID", classroomIdField);

        return panel;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints constraints,
            int row,
            String labelText,
            PlaceholderTextField field) {

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weighty = 0;

        JLabel label = new JLabel(labelText);
        label.setFont(INTER_MEDIUM);
        label.setForeground(PRIMARY_COLOR);
        panel.add(label, constraints);

        constraints.gridy = row + 1;
        constraints.insets = new Insets(4, 0, 12, 0);

        field.setFont(INTER_REGULAR);
        field.setPreferredSize(new Dimension(0, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 214, 220)),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));

        panel.add(field, constraints);
        constraints.insets = new Insets(0, 0, 12, 0);
    }

    private JPanel createActions() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setBackground(WHITE);

        JButton cancelButton = createButton("Cancel", new Color(235, 237, 241), PRIMARY_COLOR);
        cancelButton.addActionListener(_ -> dispose());

        JButton saveButton = createButton(editMode ? "Update" : "Save", PRIMARY_COLOR, WHITE);
        saveButton.addActionListener(_ -> saveEnrollment());

        panel.add(cancelButton);
        panel.add(saveButton);

        getRootPane().setDefaultButton(saveButton);

        return panel;
    }

    private JButton createButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(INTER_MEDIUM);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        return button;
    }

    private void fillForm(Enrollment enrollment) {
        studentIdField.setText(String.valueOf(enrollment.getStudentId()));
        classroomIdField.setText(String.valueOf(enrollment.getClassroomId()));
    }

    private void saveEnrollment() {
        String studentIdText = studentIdField.getText().trim();
        String classroomIdText = classroomIdField.getText().trim();

        if (studentIdText.isEmpty() || classroomIdText.isEmpty()) {
            showValidationError("Please fill in all fields.");
            return;
        }

        Long studentId;
        Long classroomId;

        try {
            studentId = Long.parseLong(studentIdText);
            classroomId = Long.parseLong(classroomIdText);

            if (studentId <= 0 || classroomId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            showValidationError("Student ID and Classroom ID must be valid positive numbers.");
            return;
        }

        if (editMode) {
            enrollment = new Enrollment(
                    originalEnrollment.getId(),
                    studentId,
                    classroomId,
                    originalEnrollment.getEnrolledAt());
        } else {
            enrollment = new Enrollment(studentId, classroomId);
        }

        dispose();
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Invalid enrollment data",
                JOptionPane.WARNING_MESSAGE);
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }
}
