package com.schoolmanager.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class NavigationPanel extends JPanel {

    private static final int SIDEBAR_WIDTH = 220;

    private static final Color SIDEBAR_COLOR
            = new Color(15, 23, 43);

    private static final Color SELECTED_COLOR
            = new Color(49, 65, 88);

    private static final Color SELECTED_TEXT_COLOR
            = new Color(249, 250, 251);

    private static final Color DEFAULT_TEXT_COLOR
            = new Color(153, 161, 175);

    private final JButton[] navigationButtons;

    public NavigationPanel(
            Runnable onStudents,
            Runnable onTeachers,
            Runnable onSubjects,
            Runnable onClassrooms,
            Runnable onEnrollments,
            Runnable onGrades
    ) {
        setLayout(new BorderLayout(0, 20));
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        setBackground(SIDEBAR_COLOR);

        setBorder(
                BorderFactory.createEmptyBorder(20, 20, 0, 20)
        );

        add(createHeader(), BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        menuPanel.setBackground(SIDEBAR_COLOR);

        navigationButtons = new JButton[]{
            createButton("Students", onStudents),
            createButton("Teachers", onTeachers),
            createButton("Subjects", onSubjects),
            createButton("Classrooms", onClassrooms),
            createButton("Enrollments", onEnrollments),
            createButton("Grades", onGrades)};

        for (int i = 0; i < navigationButtons.length; i++) {
            menuPanel.add(navigationButtons[i]);

            if (i < navigationButtons.length - 1) {
                menuPanel.add(Box.createVerticalStrut(4));
            }
        }

        add(menuPanel, BorderLayout.CENTER);

        selectedButton(navigationButtons[0]);

    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());

        header.setPreferredSize(new Dimension(180, 50));

        header.setBackground(SIDEBAR_COLOR);

        JLabel title = new JLabel("School Manager");

        title.setForeground(Color.WHITE);

        title.setFont(new Font("Inter", Font.BOLD, 20));

        title.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        header.add(title, BorderLayout.CENTER);

        return header;
    }

    private JButton createButton(
            String text,
            Runnable action
    ) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(180, 24));

        button.setMinimumSize(new Dimension(180, 24));

        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        );

        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        button.setFont(
                new Font("Inter", Font.PLAIN, 14)
        );

        button.addActionListener(event -> {
            selectedButton(button);
            action.run();
        });

        return button;
    }

    private void selectedButton(JButton selectedButton) {

        for (JButton button : navigationButtons) {

            if (button == selectedButton) {
                button.setBackground(SELECTED_COLOR);
                button.setForeground(SELECTED_TEXT_COLOR);
                button.setOpaque(true);

                button.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                                BorderFactory.createEmptyBorder(0, 10, 0, 10)
                        )
                );
            } else {
                button.setBackground(SIDEBAR_COLOR);
                button.setForeground(DEFAULT_TEXT_COLOR);
                button.setOpaque(false);

                button.setBorder(
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)
                );
            }
        }
    }
}
