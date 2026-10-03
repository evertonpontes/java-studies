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
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.schoolmanager.model.Grade;

public class GradeFormDialog extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(15, 23, 43);
    private static final Color SECONDARY_TEXT_COLOR = new Color(106, 114, 130);
    private static final Color WHITE = Color.WHITE;

    private static final Font INTER_REGULAR = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_MEDIUM = new Font("Inter", Font.PLAIN, 14);
    private static final Font INTER_SEMI_BOLD = new Font("Inter", Font.BOLD, 20);

    private final PlaceholderTextField enrollmentIdField;
    private final PlaceholderTextField valueField;
    private final PlaceholderTextField assessmentField;

    private final boolean editMode;
    private final Grade originalGrade;

    private Grade grade;

    public GradeFormDialog(Window owner) {
        this(owner, null);
    }

    public GradeFormDialog(Window owner, Grade grade) {
        super(owner, grade == null ? "Add Grade" : "Edit Grade", ModalityType.APPLICATION_MODAL);

        this.editMode = grade != null;
        this.originalGrade = grade;

        enrollmentIdField = new PlaceholderTextField("Enter enrollment ID");
        valueField = new PlaceholderTextField("Enter grade value");
        assessmentField = new PlaceholderTextField("Enter assessment");

        configureDialog();
        createContent();

        if (editMode) {
            fillForm(grade);
        }
    }

    private void configureDialog() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(540, 460);
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

        JLabel title = new JLabel(editMode ? "Edit Grade" : "Add Grade");
        title.setFont(INTER_SEMI_BOLD);
        title.setForeground(PRIMARY_COLOR);

        JLabel description = new JLabel(
                editMode ? "Update the grade's information" : "Enter the grade's information");
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

        addField(panel, constraints, 0, "Enrollment ID", enrollmentIdField);
        addField(panel, constraints, 2, "Value", valueField);
        addField(panel, constraints, 4, "Assessment", assessmentField);

        return panel;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints constraints,
            int row,
            String labelText,
            JTextField field) {

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
        saveButton.addActionListener(_ -> saveGrade());

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

    private void fillForm(Grade grade) {
        enrollmentIdField.setText(String.valueOf(grade.getEnrollmentId()));
        valueField.setText(grade.getValue().toPlainString());
        assessmentField.setText(grade.getAssessment());
    }

    private void saveGrade() {
        String enrollmentIdText = enrollmentIdField.getText().trim();
        String valueText = valueField.getText().trim().replace(',', '.');
        String assessment = assessmentField.getText().trim();

        if (enrollmentIdText.isEmpty()) {
            showValidationError("Enrollment ID is required.");
            return;
        }

        if (valueText.isEmpty()) {
            showValidationError("Grade value is required.");
            return;
        }

        if (assessment.isEmpty()) {
            showValidationError("Assessment is required.");
            return;
        }

        try {
            Long enrollmentId = Long.parseLong(enrollmentIdText);
            BigDecimal value = new BigDecimal(valueText);

            if (editMode) {
                grade = new Grade(
                        originalGrade.getId(),
                        enrollmentId,
                        value,
                        assessment,
                        originalGrade.getCreatedAt());
            } else {
                grade = new Grade(enrollmentId, value, assessment);
            }

            dispose();
        } catch (NumberFormatException e) {
            showValidationError("Enrollment ID must be an integer and value must be a valid number.");
        }
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Invalid grade data",
                JOptionPane.WARNING_MESSAGE);
    }

    public Grade getGrade() {
        return grade;
    }
}
